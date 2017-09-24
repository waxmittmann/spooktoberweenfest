package mwittmann.spooktober.unit

case class Dimensions2df(width: Float, height: Float) extends Unit2d[Float] {
  assert(width != null)
  assert(height != null)

  val xpart = width
  val ypart = height
}
