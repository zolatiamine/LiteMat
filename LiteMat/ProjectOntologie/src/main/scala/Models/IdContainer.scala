package TBox.Models

class IdContainer(var id: Long, var encodingStart: Int, var LengthId: Int) {
  def getId(): Long = id

  def setId(id: Long): Unit = {
    this.id = id
  }

  def getEncodingStart: Int = encodingStart

  def getLengthId: Int = LengthId

  override def toString: String = {
    "id = " + id + ", localLength =" + LengthId + ", encodingStart =" + encodingStart
  }

}
