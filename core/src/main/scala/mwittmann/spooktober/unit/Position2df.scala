package mwittmann.spooktober.unit

case class Position2df(x: Float, y: Float) extends Unit2d[Float] {
  def withX(_x: Float) = new Position2df(_x, y)

  def withY(_y: Float) = new Position2df(x, _y)

  def incY(yDelta: Float) = new Position2df(x, y + yDelta)

  def incX(xDelta: Float) = new Position2df(x + xDelta, y)

  override def toString: String = "(" + x + ", " + y + ")"

  override val xpart: Float = x

  override val ypart: Float = y
}
