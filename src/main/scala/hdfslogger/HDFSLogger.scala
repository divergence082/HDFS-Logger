

package hdfslogger

import java.io.File
import org.apache.hadoop.conf.Configuration
import org.apache.commons.io.FilenameUtils


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

    val storage: HStorage = new HStorage(args(0), args(1))
    val streamHandler = new StreamHandler(storage, args(2).toInt)

    streamHandler.read(System.in)
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