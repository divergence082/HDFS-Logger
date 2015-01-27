
package hdfslogger

import java.net.URI
import org.apache.commons.io.FilenameUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{Writable, SequenceFile}


/**
 * @param uri HDFS uri
 * @param dirPath A path to directory to store files
 */
class HStorage(uri: String, dirPath: String) {

  val dir = new Path(dirPath)
  val config = new Configuration()
  val hdfs: FileSystem = FileSystem.get(URI.create(uri), config)

  if (!hdfs.exists(dir)) {
    hdfs.mkdirs(dir)
  }

  /**
   * @param filename A name of file in HDFS
   * @return A full path of file to be stored
   */
  def getPath(filename: String): Path = {
    new Path(FilenameUtils.normalizeNoEndSeparator(dirPath) + "/" + filename)
  }


  /**
   * @param filename A name of file in HDFS
   * @param key A Key to store data
   * @param value Data to store
   */
  def write(filename: String, key: Writable, value: Writable): Unit = {
    val writer = new SequenceFile.Writer(hdfs, config,
      getPath(filename), key.getClass, value.getClass)

    writer.append(key, value)
    writer.close()
  }

}