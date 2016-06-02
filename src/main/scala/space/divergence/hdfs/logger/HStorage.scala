package space.divergence.hdfs.logger

import java.net.URI


/**
 * @param uri HDFS uri
 * @param dirPath A path to directory to store files
 */
class HStorage(uri: String, dirPath: String) {

  val dir = new Path(dirPath)
  val config = new Configuration()
  val hdfs: FileSystem = FileSystem.get(URI.create(uri), config)
  var writer: Option[SequenceFile.Writer] = None

  if (!hdfs.exists(dir)) {
    hdfs.mkdirs(dir)
  }


  /**
   * Opens writer
   *
   * @param filename A name of file in HDFS
   * @param key Key to be stored
   * @param value Value to be stored
   */
  def open(filename: String, key: Writable, value: Writable): Unit = {
    val path = FilenameUtils.normalizeNoEndSeparator(dirPath) + "/" + filename

    writer = Some(new SequenceFile.Writer(hdfs, config, new Path(path),
      key.getClass, value.getClass))

    println("File " + uri + path + " opened.")
  }


  /**
   * Closes writer
   */
  def close(): Unit = {
    writer match {
      case None => None
      case Some(w)  =>
        w.close()
    }
  }


  /**
   * @param key A Key to store data
   * @param value Data to store
   */
  def write(key: Writable, value: Writable): Unit = {
    writer match {
      case None =>
        println("Writer is not set")
      case Some(w) =>
        w.append(key, value)
    }
  }

}