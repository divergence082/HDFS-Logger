

package hdfslogger

import java.io.InputStream


/**
 * @constructor
 * @param marker Logs identifier
 * @param period Time laps of log file in seconds
 * @param hadoopConfPath A path to hadoop configurations
 */
class HDFSLogger(marker: String, period: Int, hadoopConfPath: String) {

  private val logger = new Logger(marker, hadoopConfPath)
  private val laps = period*1000
  private var timestampPivot = System.currentTimeMillis()
  private var data: Array[Byte] = new Array(0)


  /**
   * @param stream Input stream
   */
  def read(stream: InputStream): Unit = {
    Stream
        .continually(stream.read.toByte)
        .takeWhile(-1 !=)
        .foreach(process)
  }


  /**
   * @param byte Byte
   */
  private def process(byte: Byte) = {
    data = data :+ byte

    if (!data.isEmpty && byte == 10) {
      log(data)
      data = new Array(0)
    }
  }


  /**
   * @return Filename in format startTimestamp_stopTimestamp
   */
  private def generateFilename(): String = {
    if (System.currentTimeMillis() - timestampPivot >= laps) {
      timestampPivot = System.currentTimeMillis()
    }

    timestampPivot.toString + "_" + (timestampPivot + laps).toString
  }


  /**
   * @param data Data to log
   */
  private def log(data: Array[Byte]): Unit = {
    logger.write(generateFilename(), data)
  }

}


/**
 *
 */
object HDFSLogger {

  /**
   * @param args Command line arguments
   */
  def main(args: Array[String]) {
    val logger = new HDFSLogger(args(0), args(1).toInt, args(2))
    logger.read(System.in)
  }

}