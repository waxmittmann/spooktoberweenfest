package mwittmann.spooktober.screen

import scala.collection.mutable
import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.unit.Dimensions2df
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.unit.Vector2df
import mwittmann.spooktober.util.{Map2d, MapStorable}

class GameObjects(val dimensions: Dimensions2df) {
  val map = new Map2d[Entity](dimensions, Dimensions2df(20, 20))

  val zombies = mutable.MutableList[Zombie]()

  // Temporarily hardcoded zombie
  val zombie = new Zombie()
  map.insert(MapStorable(Position2df(10.0f, 10.0f), zombie.getDimensions, zombie))
  zombies += zombie

  val player = new Player()
  map.insert(MapStorable(Position2df(20.0f, 20.0f), player.getDimensions, player))

  def getEntities(view: View): Set[MapStorable[Entity]] = {
    map.getNodes(
      view.gameX - view.gameWidth / 2,
      view.gameY - view.gameHeight / 2,
      view.gameWidth,
      view.gameHeight)
  }

  def addZombie(zombie: MapStorable[Zombie]): Unit = {
    if (map.inBounds(zombie)) {
      map.insert(zombie)
      zombies += zombie.item
    }
  }

  def getZombies: mutable.MutableList[MapStorable[Zombie]] = {
    zombies.map(map.getEntityUnsafe)
  }

  def moveZombies(deltaSeconds: Float): Unit = {
    for (zombie <- zombies) {
      val mv = zombie.getMove(deltaSeconds)

      val mapZombie = map.getEntityUnsafe(zombie)

      val newZombiePos = mapZombie.position.add(mv)

      if (map.inBounds(newZombiePos, mapZombie.dimensions)) {
        map.move(zombie, newZombiePos)
      }
    }
  }

  def getPlayerPosition: Position2df = map.getEntityUnsafe(player).position

  def getPlayerDimensions = player.getDimensions

  def movePlayer(vector: Vector2df): Unit = {
    val playerLoc = map.getEntityUnsafe(player)
    val newPlayerPos = playerLoc.position.incX(vector.x).incY(vector.y)
    val newPlayerLoc = playerLoc.copy(position = newPlayerPos)

    if (map.checkCollision(newPlayerLoc).size == 0) {
      map.move(player, newPlayerLoc)
    } else {
      System.out.println("Blocked")
    }
  }
}
