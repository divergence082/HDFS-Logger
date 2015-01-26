

package hdfslogger

import java.io.{InputStream, File}
import org.apache.commons.io.FilenameUtils
import ru.livetex.PacketInputStreamReader


/**
 * @param period Time laps for log rotation in seconds
 */
class HDFSLogger(period: Int) {

  private val codec = PacketInputStreamReader(System.in)
  private val laps = period*1000
  private var timestampPivot = System.currentTimeMillis()
  private var data: Array[Byte] = new Array(0)


  /**
   * @return Filename in format startTimestamp_stopTimestamp
   */
  private def getFilename(): String = {
    if (System.currentTimeMillis() - timestampPivot >= laps) {
      timestampPivot = System.currentTimeMillis()
    }

    timestampPivot.toString + "_" + (timestampPivot + laps).toString
  }

  def logToFile(byte: Byte): Unit = {
    data = data :+
    codec.encode(data) match {
      case None => None
      case Some(s) =>
        storage.write(getFilename, s)
        data = new Array(0)
    }
  }


  def logToSeqFile(): Unit = {

  }


  /**
   * @param stream
   * @param log
   */
  def read(stream: InputStream, log: ()): Unit = {
    val stream: InputStream = System.in

    Stream
        .continually(stream.read.toByte)
        .taleWhile(-1 !=)
        .foreach(process)
  }

}


/**
 *
 */
object HDFSLogger {

  /**
   * @param args Command line arguments:
   *             0: url: The value of fs.default.name or fs.defaultFS,
   *             1: dirname: A directory name to store files,
   *             2: period: Time laps for log rotation.
   */
  def main(args: Array[String]) {
    val codec = new PacketInputStreamReader(System.in)

  }


  /**
   * @param dirPath A name of directory in HDFS
   * @param filename A name of file in HDFS
   * @param separator Directory separator
   * @return Full path to file
   */
  def createFilePath(dirPath: String, filename: String,
                     separator: String = File.separator): String = {
    FilenameUtils.normalizeNoEndSeparator(dirPath) + separator + filename
  }
}