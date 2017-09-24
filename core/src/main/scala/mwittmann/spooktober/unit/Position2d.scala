package mwittmann.spooktober.unit

case class Position2d(x: Float, y: Float) extends Unit2d[Float] {
  def withX(_x: Float) = new Position2d(_x, y)

  def withY(_y: Float) = new Position2d(x, _y)

  def incY(yDelta: Float) = new Position2d(x, y + yDelta)

  def incX(xDelta: Float) = new Position2d(x + xDelta, y)

  override def toString: String = "(" + x + ", " + y + ")"

  override val xpart: Float = x

  override val ypart: Float = y
}
