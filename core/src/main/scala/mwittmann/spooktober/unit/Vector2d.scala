package mwittmann.spooktober.unit

trait Vector2d[S] extends Unit2d[S] {
  val x: S
  val y: S

  val xpart: S = x
  val ypart: S = y
}

case class Vector2df(x: Float, y: Float) extends Vector2d[Float]

object Vector2df {
  def from(dimensions2df: Unit2d[Float]): Vector2df = Vector2df(dimensions2df.xpart, dimensions2df.ypart)
}

case class Vector2di(x: Int, y: Int) extends Vector2d[Int]