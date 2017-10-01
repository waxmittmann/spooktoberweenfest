package mwittmann.spooktober.entity

import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}

class Player extends Entity {
  // Todo: Inc state time
  private[entity] val stateTime = 0

  def getDimensions = new Dimensions2df(10f, 10f)

  override def texture = Assets.player
}
