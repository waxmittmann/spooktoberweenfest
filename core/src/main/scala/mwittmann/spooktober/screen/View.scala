package mwittmann.spooktober.screen

import mwittmann.spooktober.unit.{Dimensions2df, Position2df}

object View {
  val emptyView = new View(0, 0, 0, 0, 0, 0, 0, 0)
}

// Todo: At the moment, 'gameX, gameY' are the center of the screen, with the screen going from -(screenWidth/2, ..)
// to +(screenWidth/2, ...); might be nicer to set it to have gameX, gameY be lower right
class View(
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
