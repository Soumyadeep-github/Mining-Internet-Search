package com.dataModels

// This is an object.
case class RAWWarcData(
 WarcHeaders: Map[String, List[String]],
 HttpHeaders: Map[String, List[String]],
 Payload: String,
 RecordType: String,
 FileName: String,
)
