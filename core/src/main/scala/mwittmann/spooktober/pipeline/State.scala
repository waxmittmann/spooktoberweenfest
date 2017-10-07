package mwittmann.spooktober.pipeline

import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.screen.View
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.{DebugLog, Map2d, MapStorable}

import scala.collection.mutable


class ZombieState {

  val zombies = mutable.MutableList[Zombie]()

  def add(map: Map2d[Entity]): Map2d[Entity] = {
    val zombie = new Zombie()
    map.insert(MapStorable(Position2df(10.0f, 10.0f), zombie.getDimensions, zombie))
    zombies += zombie
    map
  }
}

class PlayerState(player: Player) { //}, map: Map2d[Entity]) {
  def move(
    newPlayerLoc: MapStorable[Player],
    map: Map2d[Entity]
  ): Map2d[Entity] = {
    if (map.inBounds(newPlayerLoc) && map.checkCollision(newPlayerLoc).isEmpty) {
      map.move(player, newPlayerLoc)
    } else {
      DebugLog.dprintln("Blocked")
      map
    }
  }

  def getDimensions(map: Map2d[Entity]) =
    map.getEntity(player).get.dimensions

  def getPosition(map: Map2d[Entity]): Position2df = {
    map.getEntity(player).get.position
  }
}

object PlayerState {
  def apply(map: Map2d[Entity]): (PlayerState, Map2d[Entity]) = {
    val player = new Player()
    (new PlayerState(player, map), map)
  }
}

case class State(
  zombies: ZombieState = new ZombieState(),
  player: PlayerState,
  map: Map2d[Entity],
  view: View = View.emptyView,
  input: Input = Input.NoInput,
  delta: Float = 0.0f,
  gameFactor: Float = 0.1f,
  factor: Float = 5.0f,
  playerSpeed: Float = 10,
  gameDimensions: Dimensions2df,
)

object State {
  def apply(
    gameDimensions: Dimensions2df
  ): State = {
    val map = new Map2d[Entity](gameDimensions, Dimensions2df(20, 20))
    val (playerState, mapWithPlayer) = PlayerState(map)

    State(
      player = playerState,
      map = mapWithPlayer,
      gameDimensions = gameDimensions
    )
  }
}