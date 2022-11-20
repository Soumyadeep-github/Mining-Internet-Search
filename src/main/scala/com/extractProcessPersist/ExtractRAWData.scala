package com.extractProcessPersist

import org.apache.log4j.{Level, Logger}

import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._

import com.awsOperations.S3Operator
import com.dataModels.RAWWarcData
import com.warcOperations.{WARCExtractor, WARCReader}

import scala.collection.JavaConverters.iterableAsScalaIterable

object ExtractRAWData {

  def main(args: Array[String]): Unit = {
    val spark = HouseKeeping.getSparkSession("Local")
    Logger.getLogger("org").setLevel(Level.ERROR)
    spark.sparkContext.setLogLevel("WARN")

    /*TODO: Implement this with Docker env vars or secrets.*/
    spark.sparkContext // first arg has access key
      .hadoopConfiguration.set("fs.s3a.access.key", args(0))
    spark.sparkContext // first arg has secret key
      .hadoopConfiguration.set("fs.s3a.secret.key", args(1))
    spark.sparkContext
      .hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

    val s3 = new S3Operator
    val read = new WARCReader
    val extract = new WARCExtractor
    val inputBucketName = "commoncrawl"
    val outputBucketName = "test-bucket-0-sm"

    val df = spark.read.load("s3a://commoncrawl/cc-index/table/cc-main/warc/")

    val dfProcessed = df.where("crawl = 'CC-MAIN-2018-05' AND subset = 'warc' AND " +
      "url LIKE 'https://www.google.com/search?q=%' AND content_mime_type NOT LIKE '%image%'")
      .limit(10)
      .select(col("warc_filename"),
        col("warc_record_offset").cast("int"), col("warc_record_length"))

    val columns = Array("WarcHeaders", "HttpHeaders", "PayloadContents", "RecordType", "FileName")

    val extractedRDD = dfProcessed.select("warc_filename", "warc_record_offset").rdd
      .map(x => (x.getAs[String](0), x.getAs[Int](1)))
      .map(x => (x._1, s3.getS3Client(inputBucketName, x._1)))
//      .map(x => (x._1, s3.getS3Client(inputBucketName, x._1, x._2)))
      .map(x => (x._1, iterableAsScalaIterable(read.getReader(x._2))))
//      .map(x => (x._1, read.getReader(x._2).next().get()))
      .flatMapValues(x => x)
      .map(x => RAWWarcData(extract.getWarcHeaders(x._2),
                            extract.getHttpAndOtherHeaders(x._2),
                            extract.getPayloadContents(x._2),
                            extract.getRecordType(x._2),
                            x._1)
      )

    val finalDF = spark.createDataFrame(extractedRDD).toDF(columns: _*)

    val s3OutputBucket = s"s3a://$outputBucketName/localToS3Output/"

    finalDF
      .write
      .mode(SaveMode.Overwrite)
      .format("parquet")
      .save(s3OutputBucket)

  }
}
