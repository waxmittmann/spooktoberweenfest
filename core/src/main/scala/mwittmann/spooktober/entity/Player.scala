package mwittmann.spooktober.entity

import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.MapStorable
import mwittmann.spooktober.util.MathUtils.Angle

case class PlayerStorable(
  position: Position2df,
  dimensions: Dimensions2df,
  rotation: Float = 0.0f,
  item: GameToken
) extends MapStorable[GameToken] {
  override def copy(
    position: Position2df = position,
    dimensions: Dimensions2df = dimensions,
    rotation: Angle = rotation
  ) = PlayerStorable(position, dimensions, rotation, item)
}

case class Player(token: GameToken = new GameToken {}) extends Entity {
  // Todo: Inc state time
  private[entity] val stateTime = 0

  def getDimensions = Dimensions2df(10f, 10f)

  override def texture = Assets.player
}
