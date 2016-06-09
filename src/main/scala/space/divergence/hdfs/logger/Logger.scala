package space.divergence.hdfs.logger

import java.io.InputStream

// uri = "hdfs://192.168.30.147:8020"
// path = "/tmp/mySample.txt"
class Logger(hdfsUri: String, path: String, stream: InputStream, chunkLength: Int) {

  private val _hdfs = new HDFSAdapter(hdfsUri, path)

  try {
    Iterator.continually(_log()).takeWhile(_ != -1)
  } finally {
    close()
  }

  private def _log(): Int = {
    val bytes = new Array[Byte](chunkLength)
    val bytesCount = stream.read(bytes)

    if (bytesCount > 0) {
      _hdfs.write(bytes)
    }

    bytesCount
  }

  def close(): Unit =
    _hdfs.close()
}


object Logger {
  def main(args: Array[String]): Unit = {
    new Logger(args(0), args(1), System.in, args(2).toInt)
  }
}
