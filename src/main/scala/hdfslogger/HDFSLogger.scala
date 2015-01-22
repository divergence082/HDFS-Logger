

package hdfslogger

import scala.collection.mutable.ArrayBuffer
import java.io.InputStream


/**
 * @constructor
 * @param marker Logs identifier
 * @param fsName The value of fs.default.name or fs.defaultFS properties in
 *               core-site.xml
 */
class HDFSLogger(marker: String, fsName: String) {

  var data: Array[Byte] = new Array(0)

  /**
   * @param stream Input stream
   */
  def read(stream: InputStream): Unit = {
//    while(true) {
//      println(stream.read())
//    }

    Stream
        .continually(stream.read.toByte)
        .takeWhile(-1 !=)
        .foreach(process)
  }


  /**
   * @param byte Byte
   * @return The same data
   */
  private def process(byte: Byte): Array[Byte] = {
    data = data :+ byte

    if (!data.isEmpty) {
//      another logic should be here
      log(data)
    }

    data
  }


  /**
   * @param data Data to log
   */
  private def log(data: Array[Byte]): Unit = {
    val logger = new Logger(marker, fsName)
    logger.write("123", data)
  }

}


/**
 *
 */
object HDFSLogger {

  /**
   * @param args
   */
  def main(args: Array[String]) {
    val logger = new HDFSLogger(args(0), args(1))
    logger.read(System.in)
  }

}