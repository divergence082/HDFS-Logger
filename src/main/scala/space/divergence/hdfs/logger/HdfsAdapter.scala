package space.divergence.hdfs.logger

import org.apache.hadoop.fs._
import org.apache.hadoop.conf.Configuration


class HdfsAdapter(val uri: String) {
  private val _conf = new Configuration()
  _conf.set("fs.defaultFS", uri)
  private val _fs = FileSystem.get(_conf)

  def output(path: String): FSDataOutputStream = {
    val outputPath = new Path(path)

    if (_fs.exists(outputPath)) {
      _fs.append(outputPath)
    } else {
      _fs.create(outputPath)
    }
  }

  def input(path: String): FSDataInputStream =
    _fs.open(new Path(path))

  def write(output: FSDataOutputStream, data: Array[Byte], offset: Int): Unit =
    output.write(data, offset, data.length)

  def read(input: FSDataInputStream, output: Array[Byte], offset: Int): Int =
    input.read(0, output, offset, output.length)

  def remove(path: String): Unit = {
    val rPath = new Path(path)

    if (_fs.exists(rPath)) {
      _fs.delete(rPath, true)
    }
  }

  def size(path: String): Long =
    _fs.getFileStatus(new Path(path)).getLen
}
