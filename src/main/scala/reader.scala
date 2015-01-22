

package hdfslogger

import scala.io.StdIn
import scala.collection.Buffer
import hdfslogger.Parser


/**
 * A construction, that can read and parse data from default input
 *
 * @param parser A construction that able to parse blocks of log
 */
class Reader(parser: Parser) {

  var buffer: Buffer = new Buffer(Byte)

  /**
   * @return Default input
   */
  private def read(): Byte = {
    buffer += StdIn.readByte()
  }

  /**
   * @return Parsed block of data
   */
  def process(): Buffer = {
    while (!parser.hasBlock) {
      read()
      parser.process(buffer)
    }

    val result: Buffer = parser.getBlock()
    buffer = buffer.slice(result.length, buffer.length)

    result
  }
}