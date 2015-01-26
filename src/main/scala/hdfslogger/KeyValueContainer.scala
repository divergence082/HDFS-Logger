

package hdfslogger

import org.apache.hadoop.io.{LongWritable, TupleWritable}
import org.apache.hadoop.mapred.join.TupleWritable

/**
 * Created by dev on 1/26/15.
 */
class KeyValueContainer extends Container {

  var storedData: (Writable, Writable)


  override def set(data: Array[Byte]): Unit = {
    storedData = (new LongWritable(System.currentTimeMillis()), new TupleWritable())
  }





}
