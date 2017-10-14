package mwittmann.spooktober.unit

case class Dimensions2df(width: Float, height: Float) extends Unit2d[Float] {
  def isWithin(newPosition: Position2df): Boolean = {
    newPosition.x >= 0 && newPosition.y >= 0 && newPosition.x <= width && newPosition.y <= height
  }

  assert(width != null)
  assert(height != null)

  val xpart = width
  val ypart = height

  def multiply(amount: Float): Dimensions2df = Dimensions2df(width * amount, height * amount)
  def divide(amount: Float): Dimensions2df = Dimensions2df(width / amount, height / amount)
}
