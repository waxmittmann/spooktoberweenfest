package mwittmann.spooktober.entity

import mwittmann.spooktober.unit.{Dimensions2d, Position, Vector2df}
import mwittmann.spooktober.util.GlobalRandom

// Todo: Inc state time
case class Zombie(var position: Position) {
  // Todo: Should store in renderer probably
  var stateTime: Float = GlobalRandom.random.nextFloat // So that all the anims aren't synced

  val `type`: Int = GlobalRandom.random.nextInt(2)
  val dimensions = new Dimensions2d(10.0f, 10.0f)

  def movePosition(vector: Vector2df): Unit = {
    position = position.incX(vector.x).incY(vector.y)
  }

  def getPosition: Position = position

  def setPosition(position: Position): Unit = {
    this.position = position
  }

  def getDimensions: Dimensions2d = dimensions

  private var currentDirection = new Vector2df(GlobalRandom.random.nextInt(10) - 5f, GlobalRandom.random.nextInt(10) - 5f)
  private[entity] var movedCurrentDirection = 0.0f

  def getMove(deltaSeconds: Float): Vector2df = {
    stateTime += deltaSeconds
    // If we've moved in the same direction for > 1 sec, consider changing direction
    if (movedCurrentDirection >= 1.0) { // 30% chance of direction change
      if (GlobalRandom.random.nextInt(10) > 7) currentDirection = new Vector2df(GlobalRandom.random.nextInt(10) - 5f, GlobalRandom.random.nextInt(10) - 5f)
      // Else keep going, redecide in half a second
      movedCurrentDirection = 0.5f
    }
    movedCurrentDirection += deltaSeconds
    new Vector2df(currentDirection.x * deltaSeconds, currentDirection.y * deltaSeconds)
  }
}
