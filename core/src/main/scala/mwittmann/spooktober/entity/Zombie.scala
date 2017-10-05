package mwittmann.spooktober.entity

import mwittmann.spooktober.asset.Animation
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.GlobalRandom

// Todo: Inc state time
class Zombie extends Entity {
  // Todo: Should store in renderer probably
  var stateTime: Float = GlobalRandom.random.nextFloat // So that all the anims aren't synced

  val `type`: Int = GlobalRandom.random.nextInt(2)
  val dimensions = new Dimensions2df(10.0f, 10.0f)

  def getDimensions: Dimensions2df = dimensions


  def randomMove =
    Vector2df(
      (GlobalRandom.random.nextInt(5) + 5f) * (if (GlobalRandom.random.nextBoolean()) { -1 } else 1),
      (GlobalRandom.random.nextInt(5) + 5f) * (if (GlobalRandom.random.nextBoolean()) { -1 } else 1)
    )

  private var currentDirection = randomMove
  private[entity] var movedCurrentDirection = 0.0f

  def getMove(deltaSeconds: Float): Vector2df = {
    stateTime += deltaSeconds
    // If we've moved in the same direction for > 1 sec, consider changing direction
    if (movedCurrentDirection >= 1.0) { // 30% chance of direction change
      if (GlobalRandom.random.nextInt(10) > 7) currentDirection = randomMove
      // Else keep going, redecide in half a second
      movedCurrentDirection = 0.5f
    }
    movedCurrentDirection += deltaSeconds
    Vector2df(currentDirection.x * deltaSeconds, currentDirection.y * deltaSeconds)
  }

  override def texture = {
    val animation =
      if (`type` == 0) Assets.zombieA
      else Assets.zombieB

    animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING)
  }
}
