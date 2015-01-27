

package hdfslogger

import java.io.DataInputStream
import org.apache.hadoop.io.{BytesWritable, LongWritable, IntWritable, Writable}
import org.apache.hadoop.mapred.join.TupleWritable
import ru.livetex.io.codec.PacketInputStreamReader
import ru.livetex.io.codec.PacketType.PacketType


/**
 * @param uri HDFS uri
 * @param dirPath A path to directory to store files
 * @param period Time laps for log rotation in seconds
 */
class HDFSLogger(uri: String, dirPath: String, period: Int) {

  private val codec = new PacketInputStreamReader(
    new DataInputStream(System.in))
  private val storage =new HStorage(uri, dirPath)

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
  private def log(data: Option[Packet]): Unit = {
    data match {
      case None => None
      case Some(packet: Packet) =>
        val messages: Array[Writable] = (for (item <- packet._2) yield
          new BytesWritable(item)).toArray

        val writables: Array[Writable] = Array(
          new IntWritable(packet._1.toString.toInt),
          new TupleWritable(messages))

        val key: LongWritable = new LongWritable(System.currentTimeMillis())
        val value: TupleWritable = new TupleWritable(writables)
        storage.write(getFilename(), key, value)
    }
  }


  /**
   *
   */
  def process(): Unit = {
    Stream
        .continually(codec.readPacket).foreach(log(_))
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