package com.warcOperations

import com.amazonaws.services.s3.model.S3Object
import org.netpreserve.jwarc.WarcReader

import java.io.BufferedInputStream
import java.nio.channels.FileChannel
import java.nio.file.Paths

class WARCReader {
  /* When working with files in the desktop, use path variable to read from local storage.
  * */
  def getReader(path: String, offset: Long): WarcReader =
    new WarcReader(FileChannel.open(Paths.get(path)).position(offset))

  /* When reading from files directly from S3 use this method.
  * */
  def getReader(s3Object: S3Object): WarcReader =
    new WarcReader(new BufferedInputStream(s3Object.getObjectContent))

}
