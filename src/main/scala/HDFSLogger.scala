

package hdfslogger

import scala.collection.mutable.HashMap
import scala.collection.immutable.Stream
import java.io.ByteArrayInputStream


/**
 * @constructor
 * @param marker Logs identifier
 * @param fsName The value of fs.default.name or fs.defaultFS properties in
 *               core-site.xml
 */
class HDFSLogger(marker: String, fsName: String) {

  val data: Array[Byte] = new Array(0)

  /**
   * @param stream Input stream
   */
  def read(stream: ByteArrayInputStream): Unit = {
    println("1111")
    Stream
        .continually(stream.read)
        .takeWhile(-1 !=)
        .foreach(println)
  }


//  /**
//   * @param byte Byte
//   * @return The same data
//   */
//  private def process(byte: Byte): Array[Byte] = {
//    data :+ byte
//
//    if (!data.isEmpty) {
//      another logic should be here
//      log(data)
//    }
//
//    data
//  }


//  /**
//   * @param data Data to log
//   */
//  private def log(data: Array[Byte]): Unit = {
//    var logger = Logger(marker, fsName)
//    logger.write("123", data)
//  }

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
    logger.read(new ByteArrayInputStream(new Array(0)))
  }

}