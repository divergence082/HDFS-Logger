

package hdfslogger


/**
 * Abstract Storage
 */
abstract class Storage {

  /**
   * Writes data
   */
  def write(filename: String, container: Container): Unit = {}

  /**
   * Reads data
   */
  def read(filename: String, container: Container): Unit = {}

}
