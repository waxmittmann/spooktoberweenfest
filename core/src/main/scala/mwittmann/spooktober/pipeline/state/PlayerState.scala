package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.entity.{Entity, Player}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{Map2d, MapStorable}

object PlayerState {
  def apply(
    map: Map2d[Entity],
    initialPositon: Position2df
  ): PlayerState = {
    val player = new Player()
    map.insert(player, initialPositon, player.getDimensions)
    new PlayerState(player)
  }
}

class PlayerState(player: Player) {
  def move(newPlayerLoc: Position2df)(implicit map: Map2d[Entity]): Boolean = map.moveIfPossible(player, newPlayerLoc)

  def getPlayerStorable(implicit map: Map2d[Entity]): MapStorable[Entity] =
    map.getEntity(player).getOrElse(throw new Exception("Player not on map!"))

  def getDimensions(implicit map: Map2d[Entity]) =
    map.getEntity(player).get.dimensions

  def getPosition(implicit map: Map2d[Entity]): Position2df = {
    map.getEntity(player).get.position
  }
}