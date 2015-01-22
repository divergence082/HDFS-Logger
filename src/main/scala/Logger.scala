

package hdfslogger

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}


/**
 * Construction, that allows to write logs to hdfs
 *
 * @constructor
 * @param marker Logs identifier
 * @param fsName The value of fs.default.name or fs.defaultFS properties in
 *               core-site.xml
 */
class Logger(marker: String, fsName: String) {

  private val config = new Configuration()
  config.set("fs.default.name", fsName)

  private val hdfs = FileSystem.get(config)
  private val separator: String = "/"


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
