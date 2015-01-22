

package hdfslogger

import scala.collection.mutable.{Buffer, Queue}


/**
 * A construction that handle data and divide it into log blocks
 *
 * @constructor
 */
class Parser() {

  var blocks: Queue = new Queue()

  /**
   * @param buffer Data to handle
   */
  def process(buffer: Buffer): Unit = {
//    do some shit
//    val someResult
//    if (someCondition with someResult) {
//        blocks ++= someResult
//    }
  }

  /**
   * @return Is queue has ready elements to return
   */
  def hasBlock: Boolean = {
    !blocks.isEmpty()
  }

  /**
   * @return The first element of the results queue
   */
  def getBlock: Buffer = {
    blocks.head()
  }
}