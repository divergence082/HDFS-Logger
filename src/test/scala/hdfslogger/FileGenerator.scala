
package hdfslogger

import java.io.{DataOutputStream, FileOutputStream}

import ru.livetex.io.codec.{PacketOutputStreamWriter, PacketType}


class FileGenerator {

  def main(args: Array[String]): Unit = {
    val writer = new PacketOutputStreamWriter(
      new DataOutputStream(new FileOutputStream("test")))

    writer.write(
      PacketType.BINARY_TYPE,
      Array("123".getBytes, "456".getBytes, "789".getBytes))

    println("OK")
  }

}
