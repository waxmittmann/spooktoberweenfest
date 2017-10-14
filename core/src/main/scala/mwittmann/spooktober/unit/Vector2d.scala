package mwittmann.spooktober.unit

import mwittmann.spooktober.util.MathUtils

trait Vector2d[S] extends Unit2d[S] {
  val x: S
  val y: S

  val xpart: S = x
  val ypart: S = y
}

case class Vector2df(x: Float, y: Float) extends Vector2d[Float] {
  def mult(amountTravelled: Float): Vector2df = Vector2df(x * amountTravelled, y * amountTravelled)


  def normalize: Vector2df = {
    val length = MathUtils.length(this)
    Vector2df((x / length).toFloat, (y / length).toFloat)
  }

}

object Vector2df {
  def from(dimensions2df: Unit2d[Float]): Vector2df = Vector2df(dimensions2df.xpart, dimensions2df.ypart)
}

case class Vector2di(x: Int, y: Int) extends Vector2d[Int]