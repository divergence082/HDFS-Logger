

package hdfslogger

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}


/**
 * Construction, that allows to write logs to hdfs
 *
 * @constructor
 * @param marker Logs identifier
 * @param hadoopConfPath A path to hadoop configurations
 */
class Logger(marker: String, hadoopConfPath: String) {

  private val separator: String = "/"
  private val config = new Configuration()

  config.addResource(hadoopConfPath + separator + "core-site.xml")
  config.addResource(hadoopConfPath + separator + "hdfs-site.xml")

  private val hdfs = FileSystem.get(config)


  /**
   * @param fileName Name of the file to be written
   * @param data Bytes to be written
   */
  def write(fileName: String, data: Array[Byte]): Unit = {
    val dir = new Path(marker)
    val path = new Path(marker + separator + fileName)

    if (!hdfs.exists(dir)) {
      hdfs.mkdirs(dir)
    }

    if (hdfs.exists(path)) {
      val input: FSDataOutputStream = hdfs.append(path)
      input.write(data)
      input.close()
    } else {
      val input: FSDataOutputStream = hdfs.create(path)
      input.write(data)
      input.close()
    }
  }
}
