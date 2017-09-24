package mwittmann.spooktober.unit

case class Position(x: Float, y: Float) {
  def withX(_x: Float) = new Position(_x, y)

  def withY(_y: Float) = new Position(x, _y)

  def incY(yDelta: Float) = new Position(x, y + yDelta)

  def incX(xDelta: Float) = new Position(x + xDelta, y)

  override def toString: String = "(" + x + ", " + y + ")"
}
