

package hdfslogger

import java.net.URI
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{
                              FileSystem,
                              FSDataInputStream,
                              FSDataOutputStream,
                              Path
                            }


/**
 * @param uri HDFS uri
 * @param dirPath A path to directory to store files
 */
class HStorage(uri: String, dirPath: String) extends Storage {

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
    new Path(HDFSLogger.createFilePath(dirPath, filename, "/"))
  }


  /**
   * @param filename Name of the file to store to HDFS
   * @param container Container to store data was read
   */
  def read(filename: String, container: Array[Byte]): Unit = {

    val stream: FSDataInputStream = hdfs.open(getPath(filename))

    Stream
        .continually(stream.read(container).toByte)
        .takeWhile(-1 !=)

    stream.close()
  }


  /**
   * @param filename A name of file in HDFS
   * @param container Container for data to store
   */
  def write(filename: String, container: Array[Byte]): Unit = {

    val path: Path = getPath(filename)
    val stream: FSDataOutputStream = if (hdfs.exists(path)) {
      hdfs.append(path)
    } else {
      hdfs.create(path)
    }

    stream.write(container)
    stream.close()
  }


}
