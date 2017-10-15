package mwittmann.spooktober.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import mwittmann.spooktober.asset.Animation
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.pipeline.state.State
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.MathUtils.Angle
import mwittmann.spooktober.util.{GlobalRandom, MapStorable, MathUtils}

case class ZombieStorable(
  position: Position2df,
  dimensions: Dimensions2df,
  rotation: Float = 0.0f,
  item: Zombie
) extends MapStorable[Zombie] {
  override def copy(
    position: Position2df = position,
    dimensions: Dimensions2df = dimensions,
    rotation: Angle = rotation
  ) = ZombieStorable(position, dimensions, rotation, item)
}

case class ZombieAttributes(
  hitPoints: Int,
  currentHitPoints: Int,
  dimensions: Dimensions2df,
  movedCurrentDirection: Float,
  currentDirection: (Vector2df, Angle)
)

trait ZombieAI {
  def update(delta: Float, attributes: ZombieAttributes): ZombieAttributes
}

object RandomZombieAI extends ZombieAI {

  def update(delta: Float, attributes: ZombieAttributes): ZombieAttributes = {
    // If we've moved in the same direction for > 1 sec, consider changing direction
    var newCurrentDirection = attributes.currentDirection
    var newMovedCurrentDirection = attributes.movedCurrentDirection
    if (attributes.movedCurrentDirection >= 1.0) { // 30% chance of direction change
      if (true) {
      //if (GlobalRandom.random.nextInt(10) > 7) {
        newCurrentDirection = randomMove
        newMovedCurrentDirection = 0
      } else {
        // Else keep going, redecide in half a second
        newMovedCurrentDirection = 0.5f
      }
    }
    newMovedCurrentDirection += delta

    println(s"Changed dir from: ${attributes.currentDirection} to $newCurrentDirection")

    attributes.copy(
      movedCurrentDirection = newMovedCurrentDirection,
      currentDirection = newCurrentDirection,
    )
  }

  private def randomMove: (Vector2df, Float) = {
    val vector = Vector2df(
      (GlobalRandom.random.nextInt(5) + 5f) * (if (GlobalRandom.random.nextBoolean()) { -1 } else 1),
      (GlobalRandom.random.nextInt(5) + 5f) * (if (GlobalRandom.random.nextBoolean()) { -1 } else 1)
    )
    makeMove(vector)
  }

  private def makeMove(vector: Vector2df): (Vector2df, Float) = {
    val angle = MathUtils.getAngle2(MathUtils.upVector, vector)
    (vector, angle)
  }
}

case class Zombie(
  animation: Animation,
  zombieAttributes: ZombieAttributes = ZombieAttributes(
    hitPoints = 100,
    currentHitPoints = 100,
    dimensions = Dimensions2df(10f, 10f),
    movedCurrentDirection = 0,
    // Todo: Better
//    currentDirection = (Vector2df(GlobalRandom.random.nextFloat() * 5, GlobalRandom.random.nextFloat() * 5), 0)
    currentDirection = (Vector2df(0, 0), 0)
  ),
  stateTime: Float = GlobalRandom.random.nextFloat,
  zombieAI: ZombieAI = RandomZombieAI
) extends Entity {
  def getKeyframe: TextureRegion = animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING)

  override def texture = getKeyframe
}
