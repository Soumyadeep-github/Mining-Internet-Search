package com.awsOperations

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.{GetObjectRequest, S3Object}

class S3Operator {

  def getS3Client(bucketName: String, pathFileName: String): S3Object =
    AmazonS3ClientBuilder.standard().build().getObject(bucketName, pathFileName)

  def getS3Client(bucketName: String, pathFileName: String, offset: Long): S3Object = {
    val request : GetObjectRequest = new GetObjectRequest(bucketName, pathFileName)
    request.setRange(offset)
    AmazonS3ClientBuilder.standard().build().getObject(request)
  }

  // For some reason this throws an EOF Error
  def getS3Client(bucketName: String, pathFileName: String, offset: Long, length: Long): S3Object = {
    val request : GetObjectRequest = new GetObjectRequest(bucketName, pathFileName)
    request.setRange(offset, length)
    AmazonS3ClientBuilder.standard().build().getObject(request)
  }

  /*
  * TODO : Implement write to S3 method here.*/

}
