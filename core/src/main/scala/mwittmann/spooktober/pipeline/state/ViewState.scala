package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.unit.{Dimensions2df, Position2df}

object ViewState {
  val emptyView = new ViewState(0, 0, 0, 0, 0, 0, 0, 0)
}

class ViewState(
  val gameX: Float,
  val gameY: Float,
  val gameWidth: Float,
  val gameHeight: Float,
  val screenX: Float,
  val screenY: Float,
  val screenWidth: Float,
  val screenHeight: Float
) {
  def viewPositionGame: Position2df = Position2df(gameX, gameY)
  def viewDimensionsGame: Dimensions2df = Dimensions2df(gameWidth, gameHeight)

  val xFactor: Float  = screenWidth / gameWidth
  val yFactor: Float = screenHeight / gameHeight

  def translateX(x: Float): Float = (x - gameX) * xFactor + screenX

  def translateY(y: Float): Float = (y - gameY) * yFactor + screenY

  def translateWidth(x: Float): Float = x * xFactor

  def translateHeight(y: Float): Float = y * yFactor
}
