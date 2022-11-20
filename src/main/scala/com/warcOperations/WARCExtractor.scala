package com.warcOperations

import org.apache.commons.io.IOUtils
import org.netpreserve.jwarc.{MessageBody, WarcMetadata, WarcRecord, WarcRequest, WarcResource, WarcResponse, Warcinfo}

import java.nio.charset.StandardCharsets
import java.util.{List => JavaList, Map => JavaMap}
import scala.collection.JavaConverters._

//noinspection ScalaUnusedSymbol
class WARCExtractor{

  // Helper method
  def javaToScalaMap(javaMapObj: JavaMap[String, JavaList[String]]): Map[String, List[String]] =
    javaMapObj.asScala.toMap.mapValues(_.asScala.toList)

  def getWarcHeaders(record: WarcRecord): Map[String, List[String]] = {
    val recHeadersMap = record.headers().map()
    javaToScalaMap(recHeadersMap) ++ Map("WARC-Version" -> List(record.version().toString))
  }

  // Helper method
  def getExtraHeaders(record: WarcRecord): List[String] = record match {
    case resp: WarcResponse => val http = resp.http()
      List(http.status().toString, http.version().toString, http.reason())
    case req: WarcRequest => val http = req.http()
      List(http.method(), http.version().toString)
    case warcInfo: Warcinfo => List(warcInfo.version().toString)
    case warcMeta: WarcMetadata => List(warcMeta.target(), warcMeta.version().toString)
    case warcResource: WarcResource => List(warcResource.version().toString, warcResource.target())
  }

  def getHttpAndOtherHeaders(record: WarcRecord): Map[String, List[String]] = {
    val currentHeaders =  record match {
      case warcResp: WarcResponse => javaToScalaMap(warcResp.http().headers().map())
      case warcReq: WarcRequest => javaToScalaMap(warcReq.http().headers().map())
      case warcInfo: Warcinfo => javaToScalaMap(warcInfo.headers().map()) ++ javaToScalaMap(warcInfo.fields().map())
      case warcMeta: WarcMetadata => javaToScalaMap(warcMeta.headers().map()) ++ javaToScalaMap(warcMeta.fields().map())
      case warcResource: WarcResource => javaToScalaMap(warcResource.headers().map())
      case _ => Map("Headers-Not-found" -> List("Type Unknown"))
    }
    currentHeaders ++ Map("Extra-Headers" -> getExtraHeaders(record))
  }

  // Helper method
  def getBodyFromRec(body: MessageBody): String = IOUtils.toString(body.stream(), StandardCharsets.UTF_8)

  def getPayloadContents(record: WarcRecord): String = record match {
    case warcResp: WarcResponse => getBodyFromRec(warcResp.http().body())
    case warcReq: WarcRequest => getBodyFromRec(warcReq.http().body())
    case warcInfo: Warcinfo => getBodyFromRec(warcInfo.body())
    case warcMeta: WarcMetadata => getBodyFromRec(warcMeta.body())
    case warcResource: WarcResource => getBodyFromRec(warcResource.body())
  }

  def getRecordType(record: WarcRecord): String = record.`type`()
}
