package mwittmann.spooktober.entity

import mwittmann.spooktober.unit.{Dimensions2d, Position2d, Vector2df}

class Player(var position: Position2d) {
  // Todo: Inc state time
  private[entity] val stateTime = 0

  def getPosition: Position2d = position

  def getDimensions = new Dimensions2d(10f, 10f)

  def setPosition(p: Position2d): Unit = {
    this.position = p
  }

  def movePosition(vector: Vector2df): Unit = {
    position = position.incX(vector.x).incY(vector.y)
  }
}
