package hdfslogger

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{BytesWritable, LongWritable, Text}
import org.scalatest._


class HStorageSpec extends FlatSpec with Matchers {
  val config = new Configuration()

  val storage = new HStorage("hdfs://localhost/", "StorageSpec")

  "A Storage" should "writes and reads SequenceFile" in {
    val value = "test value".getBytes()

    storage.write("WriteSequenceFile", value)

    val result: (LongWritable, BytesWritable) =
      storage.read("WriteSequenceFile")

    assert(result._2 == new BytesWritable(value))
  }

}
