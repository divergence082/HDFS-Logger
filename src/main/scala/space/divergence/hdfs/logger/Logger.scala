package space.divergence.hdfs.logger

import java.io.{BufferedReader, InputStreamReader, InputStream}
import org.slf4j.LoggerFactory
import org.apache.hadoop.fs.FSDataOutputStream


class Logger(hdfs: HdfsAdapter) {

  private val _logger = LoggerFactory.getLogger(this.getClass)

  def log(path: String, stream: InputStream, chunkLength: Int): Unit = {
    _logger.info(s"Log to ${hdfs.uri}$path by $chunkLength bytes")

    val output = hdfs.output(path)
    val chunk = new Array[Byte](chunkLength)

    Iterator
      .continually(stream.read(chunk))
      .takeWhile(_ != -1)
      .foreach { read =>
        val wChunk = chunk.slice(0, read)
        _logger.trace(s"read $read bytes from stream")
        hdfs.write(output, wChunk, 0)
        _logger.trace(s"wrote '${new String(wChunk)}' to hdfs")
      }

    output.close()
  }
}


object Logger {

  def apply(hdfsUri: String): Logger =
    new Logger(new HdfsAdapter(hdfsUri))

  def main(args: Array[String]): Unit = {

    args match {
      case Array(hdfsUri: String, hdfsPath: String, chunkLength: String) =>
        Logger(hdfsUri).log(hdfsPath, System.in, chunkLength.toInt)
      case _ =>
        throw new Exception("Invalid arguments!")
    }
  }
}
