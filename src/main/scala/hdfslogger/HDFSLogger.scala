

package hdfslogger

import org.apache.hadoop.io.{LongWritable, IntWritable, Writable}
import org.apache.hadoop.mapred.join.TupleWritable
import ru.livetex.io.codec.{PacketInputStreamReader, PacketType}


/**
 * @param uri HDFS uri
 * @param dirPath A path to directory to store files
 * @param period Time laps for log rotation in seconds
 */
class HDFSLogger(uri: String, dirPath: String, period: Int) {

  private val codec = PacketInputStreamReader(System.in)
  private val storage = HStorage(uri, dirPath)

  private val laps = period*1000
  private var timestampPivot = System.currentTimeMillis()

  type Packet = (PacketType, Seq[Array[Byte]])


  /**
   * @return Filename in format startTimestamp_stopTimestamp
   */
  private def getFilename(): String = {
    if (System.currentTimeMillis() - timestampPivot >= laps) {
      timestampPivot = System.currentTimeMillis()
    }

    timestampPivot.toString + "_" + (timestampPivot + laps).toString
  }


  /**
   * @param data Parsed data
   */
  private def log(data: Packet): Unit = {

    val sequences: Seq[Array[Byte]] = data._2
    val writables: Array[Writable] = (
        new IntWritable(data._1),
        new TupleWritable(sequences.foreach(Array[Byte] _)))

    val key: LongWritable = new LongWritable(System.currentTimeMillis())
    val value: TupleWritable = new TupleWritable(writables)
    storage.write(getFilename(), key, value)
  }


  /**
   *
   */
  def process(): Unit = {
    Stream
        .continually(codec.readPacket)
        .takeWhile(-1 !=)
        .foreach{result: Packet => result match {
            case None => None
            case Some(s) => log(result)
          }
        }
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
    val logger = new HDFSLogger(args(0), args(1), args(2).toInt)
    logger.process()
  }

}