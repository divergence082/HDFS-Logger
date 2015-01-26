package hdfslogger

import org.apache.hadoop.io.{SequenceFile, Writable}


/**
 * @param uri HDFS uri
 * @param dirPath A path to directory to store files
 */
class HSeqFileStorage(uri: String, dirPath: String)
    extends HStorage(uri, dirPath) {

  /**
   * @param filename Name of the file to store to HDFS
   * @param container Container to store data was read: A key-value pair
   */
  def read(filename: String, container: (Writable, Writable)): Unit = {
    val key: Writable = container._1
    val value: Writable = container._2
    val reader = new SequenceFile.Reader(hdfs, getPath(filename), config)

    reader.next(key, value)
    reader.close()
  }


  /**
   * @param filename A name of file in HDFS
   * @param container Container to store data was read: A key-value pair
   */
  def write(filename: String, container: (Writable, Writable)): Unit = {
    val key: Writable = container._1
    val value: Writable = container._2
    val writer = new SequenceFile.Writer(hdfs, config,
      getPath(filename), key.getClass, value.getClass)

    writer.append(key, value)
    writer.close()
  }

}