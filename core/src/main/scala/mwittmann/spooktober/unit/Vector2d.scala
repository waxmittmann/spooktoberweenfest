package mwittmann.spooktober.unit

trait Vector2d[S] extends Unit2d[S] {
  val x: S
  val y: S

  val xpart: S = x
  val ypart: S = x
}

case class Vector2df(x: Float, y: Float) extends Vector2d[Float]

case class Vector2di(x: Int, y: Int) extends Vector2d[Int]