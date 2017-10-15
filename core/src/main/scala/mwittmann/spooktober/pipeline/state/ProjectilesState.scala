package mwittmann.spooktober.pipeline.state

import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}

case class Projectile(
  position: Position2df,
  dimensions: Dimensions2df,
  direction: Vector2df,
  speed: Float,
  distanceTravelled: Float
) {
  def centerPosition: Position2df = {
    Position2df(position.x + dimensions.width / 2, position.y + dimensions.height / 2)
  }

  def toRectangle = new Rectangle(position.x, position.y, dimensions.width, dimensions.height)
}

case class ProjectilesState(
  projectiles: List[Projectile]
) {
  def add(position2df: Position2df, direction: Vector2df, speed: Float): ProjectilesState =
    this.copy(projectiles = Projectile(position2df, Dimensions2df(2, 2), direction, speed, 0) :: projectiles)

  def update(
    gameDimensions: Dimensions2df,
    delta: Float
  ): ProjectilesState = {
    this.copy(
      projectiles = projectiles.flatMap(projectile => {
        val amountTravelled = projectile.speed * delta
        val newPosition = projectile.position.inc(projectile.direction.mult(amountTravelled))
        if (gameDimensions.isWithin(newPosition)) {
          Some(projectile.copy(
            position = newPosition,
            distanceTravelled = projectile.distanceTravelled + amountTravelled
          ))
        } else None
      })
    )
  }
}
