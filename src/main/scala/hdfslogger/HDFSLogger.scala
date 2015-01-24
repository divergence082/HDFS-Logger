

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
   *             0: dirname: A directory name to store files,
   *             1: hadoopDir: A path to hadoop configurations,
   *                           usually /etc/hadoop/conf,
   *             2: writerType: 'File' for usual data storing and
   *                            'SeqFile' for file based data structures,
   *             3: period: Time laps for log rotation.
   */
  def main(args: Array[String]) {

    val hadoopDir: String = args(1)
    val config = new Configuration()

    config.addResource(createFilePath(hadoopDir, "core-site.xml"))
    config.addResource(createFilePath(hadoopDir, "hdfs-site.xml"))

    val writer: HDFSWriter = args(2) match {
      case "SeqFile" => new SeqFileWriter(config, args(0))
      case _ => new HDFSWriter(config, args(0))
    }

    val streamHandler = new StreamHandler(writer, args(3).toInt)
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