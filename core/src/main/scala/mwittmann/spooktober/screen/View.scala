package mwittmann.spooktober.screen

class View(val gameX: Float, val gameY: Float, val gameWidth: Float, val gameHeight: Float, val screenX: Float, val screenY: Float, val screenWidth: Float, val screenHeight: Float) {
  val xFactor: Float  = screenWidth / gameWidth
  val yFactor: Float = screenHeight / gameHeight

  def translateX(x: Float): Int = ((x - gameX) * xFactor + screenX).toInt

  def translateY(y: Float): Int = ((y - gameY) * yFactor + screenY).toInt

  def translateWidth(x: Float): Int = (x * xFactor).toInt

  def translateHeight(y: Float): Int = (y * yFactor).toInt
}
