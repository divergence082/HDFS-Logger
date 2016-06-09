package space.divergence.hdfs.logger

import org.apache.hadoop.fs._
import org.apache.hadoop.conf.Configuration


class HDFSAdapter(uri: String, path: String) {

  private val _conf = new Configuration()
  _conf.set("fs.defaultFS", uri)
  private val _fs = FileSystem.get(_conf)
  private val _outputPath = new Path(path)
  private val _output = _getOutput()
  private var _offset = 0

  private def _getOutput(): FSDataOutputStream =
    if (_fs.exists(_outputPath)) {
      _fs.append(_outputPath)
    } else {
      _fs.create(_outputPath)
    }

  def write(data: Array[Byte]): Unit = {
    val len = data.length
    _output.write(data, _offset, len)
    _offset += len
  }

  def close(): Unit =
    _output.close()
}
