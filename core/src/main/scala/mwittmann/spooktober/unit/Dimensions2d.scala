package mwittmann.spooktober.unit

case class Dimensions2d(x: Float, y: Float) extends Unit2d[Float] {
  assert(x != null)
  assert(y != null)

  val xpart = x
  val ypart = y
}
