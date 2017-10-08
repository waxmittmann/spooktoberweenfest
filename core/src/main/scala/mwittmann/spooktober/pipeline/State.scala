package mwittmann.spooktober.pipeline

import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.screen.View
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.{DebugLog, Map2d, MapStorable}

import scala.collection.mutable


class ZombieState {
  val zombies = mutable.MutableList[Zombie]()

  def add(position2df: Position2df)(implicit map: Map2d[Entity]): Unit = {
    val zombie = new Zombie()
    map.insertIfPossible(MapStorable(position2df, zombie.getDimensions, zombie))
    zombies += zombie
  }
}

class PlayerState(player: Player) {
  def move(
    newPlayerLoc: Position2df
  )(implicit map: Map2d[Entity]): Boolean = {
    println(s"Trying to move player to ${newPlayerLoc}")
    map.moveIfPossible(player, newPlayerLoc)
  }

  def getPlayerStorable(implicit map: Map2d[Entity]): MapStorable[Entity] =
    map.getEntity(player).getOrElse(throw new Exception("Player not on map!"))

  def getDimensions(implicit map: Map2d[Entity]) =
    map.getEntity(player).get.dimensions

  def getPosition(implicit map: Map2d[Entity]): Position2df = {
    map.getEntity(player).get.position
  }
}

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
) {
  implicit val imap = map
}

object State {
  def apply(
    gameDimensions: Dimensions2df
  ): State = {
    val map = new Map2d[Entity](gameDimensions, Dimensions2df(20, 20))
    val playerState = PlayerState(map, Position2df(50, 50))

    State(
      player = playerState,
      gameDimensions = gameDimensions,
      map = map
    )
  }
}