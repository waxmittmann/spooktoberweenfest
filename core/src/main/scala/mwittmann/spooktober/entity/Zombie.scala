package mwittmann.spooktober.entity

import mwittmann.spooktober.asset.Animation
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.unit.{Dimensions2df, Vector2df}
import mwittmann.spooktober.util.MathUtils.Angle
import mwittmann.spooktober.util.{GlobalRandom, MathUtils}

class Zombie extends Entity {
  val `type`: Int = GlobalRandom.random.nextInt(2)
  val dimensions = Dimensions2df(10.0f, 10.0f)

  private var currentDirection: (Vector2df, Angle) = randomMove
  private var movedCurrentDirection = 0.0f
  private var stateTime: Float = GlobalRandom.random.nextFloat // So that all the anims aren't synced

  def getDimensions: Dimensions2df = dimensions

  def randomMove: (Vector2df, Float) = {
    val vector = Vector2df(
      (GlobalRandom.random.nextInt(5) + 5f) * (if (GlobalRandom.random.nextBoolean()) { -1 } else 1),
      (GlobalRandom.random.nextInt(5) + 5f) * (if (GlobalRandom.random.nextBoolean()) { -1 } else 1)
    )
    makeMove(vector)
  }

  def makeMove(vector: Vector2df): (Vector2df, Float) = {
    val angle = MathUtils.getAngle2(MathUtils.upVector, vector)
    (vector, angle)
  }

  def getMove(deltaSeconds: Float): (Vector2df, Angle) = {
    stateTime += deltaSeconds
    // If we've moved in the same direction for > 1 sec, consider changing direction
    if (movedCurrentDirection >= 1.0) { // 30% chance of direction change
      if (GlobalRandom.random.nextInt(10) > 7) {
        currentDirection = randomMove
      }

      // Else keep going, redecide in half a second
      movedCurrentDirection = 0.5f
    }
    movedCurrentDirection += deltaSeconds

    (
      Vector2df(
        currentDirection._1.x * deltaSeconds,
        currentDirection._1.y * deltaSeconds
      ),
      currentDirection._2
    )
  }

  override def texture = {
    val animation =
      if (`type` == 0) Assets.zombieA
      else Assets.zombieB

    animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING)
  }
}
