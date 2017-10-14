package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.unit.{Dimensions2df, Position2df}

object ViewState {
  val emptyView = ViewState(0, 0, 0, 0, 0, 0, 0, 0)
}

case class ViewState(
  gameX: Float,
  gameY: Float,
  gameWidth: Float,
  gameHeight: Float,
  screenX: Float,
  screenY: Float,
  screenWidth: Float,
  screenHeight: Float
) {
  def viewPositionGame: Position2df = Position2df(gameX, gameY)
  def viewDimensionsGame: Dimensions2df = Dimensions2df(gameWidth, gameHeight)

  val xFactor: Float  = screenWidth / gameWidth
  val yFactor: Float = screenHeight / gameHeight

  def translateX(gamePosX: Float): Float = (gamePosX - gameX) * xFactor + screenX

  def translateY(gamePosY: Float): Float = (gamePosY - gameY) * yFactor + screenY

  def untranslateX(screenPosX: Int): Float = (screenPosX.toFloat - screenX) / xFactor + gameX

  def untranslateY(screenPosY: Int): Float = (screenPosY.toFloat - screenY) / yFactor + gameY

  def translateWidth(x: Float): Float = x * xFactor

  def translateHeight(y: Float): Float = y * yFactor
}
