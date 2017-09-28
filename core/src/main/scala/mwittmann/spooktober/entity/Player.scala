package mwittmann.spooktober.entity

import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}

class Player extends Entity {
  // Todo: Inc state time
  private[entity] val stateTime = 0

//  def getPosition: Position2df = position

  def getDimensions = new Dimensions2df(10f, 10f)

//  def setPosition(p: Position2df): Unit = {
//    this.position = p
//  }

//  def movePosition(vector: Vector2df): Unit = {
//    position = position.incX(vector.x).incY(vector.y)
//  }
}
