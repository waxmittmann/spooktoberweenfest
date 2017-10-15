package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.entity.{Entity, GameToken, Player, PlayerStorable}
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.{Map2d, MapStorable}

object PlayerState {
  def apply(
    map: Map2d[GameToken],
    initialPositon: Position2df
  ): PlayerState = {
    val player = new Player()
    map.insert(PlayerStorable(initialPositon, player.getDimensions, 0, player.token))
    new PlayerState(player)
  }
}

case class PlayerState(
  player: Player,
  lastFiredAgo: Float = 0.0f
) {

  def setRotation(newAngle: Float)(implicit map: Map2d[GameToken]): Unit = {
    map.setRotation(player.token, newAngle)
  }

  def getCenterPosition(implicit map: Map2d[GameToken]): Position2df =
    getPosition.add(Vector2df.from(getDimensions.divide(2)))

  def move(newPlayerLoc: Position2df)(implicit map: Map2d[GameToken]): Boolean = map.moveIfPossible(player.token, newPlayerLoc)

  def getPlayerStorable(implicit map: Map2d[GameToken]): MapStorable[GameToken] =
    map.getEntity(player.token).getOrElse(throw new Exception("Player not on map!"))

  def getDimensions(implicit map: Map2d[GameToken]): Dimensions2df =
    map.getEntity(player.token).get.dimensions

  def getPosition(implicit map: Map2d[GameToken]): Position2df = {
    map.getEntity(player.token).get.position
  }
}