package hdfslogger


/**
 *
 */
abstract class Container {

  var storedData: Any

  /**
   * @param data Data to store somewhere
   */
  def set(data: Array[Byte]): Unit = {
    storedData = data
  }

  /**
   * @return Structured data
   */
  def get(): Any = {
    storedData
  }

}
