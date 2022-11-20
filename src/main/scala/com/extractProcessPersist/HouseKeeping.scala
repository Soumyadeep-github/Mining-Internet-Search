package com.extractProcessPersist

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

//object Environment extends Enumeration {
//  type Environment = String
//  val LOCAL = "Local"
//  val TEST = "Test"
//  val PROD = "Production"
//}

//noinspection ScalaUnusedSymbol
object HouseKeeping {

  /* TODO: Use an environment variable within Docker to fetch correct SparkSession
      as per specified mode of operation.
      Like on AWS Cloud or Local.
      Provision for fetching Spark cluster configurations from spark-submit.
  * */
   def getSparkConfLocalES(appName: String = "LocalWarcProcessingES"): SparkConf = {
     val conf = new SparkConf()
     conf.setAll(Array(
       ("spark.master", "local[*]"),
       ("spark.app.name", appName),
       ("spark.es.nodes", "localhost"),
       ("spark.es.port","9200"),
       ("spark.es.index.auto.create","true"),
       ("spark.es.nodes.wan.only","true")
     ))
   }

  def getSparkConfLocal(appName: String = "LocalWarcProcessing"): SparkConf = {
    val conf = new SparkConf()
    conf.setAll(Array(
      ("spark.master", "local[*]"),
      ("spark.app.name", appName)
    ))
  }


  def getSparkConf(appName: String = "WarcProcessingOnCloud"): SparkConf = {
    val conf = new SparkConf()
    conf.setAll(Array(
      ("spark.app.name", appName)))
  }

  def getSparkConf(appName: String = "WarcProcessingOnCloud", otherConfigs: Array[(String, String)]): SparkConf = {
    val conf = new SparkConf()
    conf.setAll(Array(
      ("spark.app.name", appName))
      ++ otherConfigs
    )
  }

   def getSparkSession(environ: String): SparkSession = environ match {
     case locES if locES == "LocalES" => SparkSession.builder().config(getSparkConfLocalES()).getOrCreate()
     case loc if loc == "Local" => SparkSession.builder().config(getSparkConfLocal()).getOrCreate()
     case cl if cl == "Cloud" => SparkSession.builder().config(getSparkConf()).getOrCreate()
   }

   def getSparkSession(environ: String, appName: String): SparkSession = environ match {
     case locES if locES == "LocalES" => SparkSession.builder().config(getSparkConfLocalES(appName)).getOrCreate()
     case loc if loc == "Local" => SparkSession.builder().config(getSparkConfLocal(appName)).getOrCreate()
     case cl if cl == "Cloud" => SparkSession.builder().config(getSparkConf(appName)).getOrCreate()
   }

   def getSparkSession(environ: String, appName: String, otherConfigs: Array[(String, String)]): SparkSession =
     SparkSession.builder().config(getSparkConf(appName, otherConfigs)).getOrCreate()



}
