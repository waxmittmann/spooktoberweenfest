package mwittmann.spooktober.unit

trait Vector2d[S] {
  val x: S
  val y: S
}


case class Vector2df(x: Float, y: Float) extends Vector2d[Float]