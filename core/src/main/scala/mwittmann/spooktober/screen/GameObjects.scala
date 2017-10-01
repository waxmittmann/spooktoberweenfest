package mwittmann.spooktober.screen

import scala.collection.mutable
import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.unit.Dimensions2df
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.unit.Vector2df
import mwittmann.spooktober.util.{Map2d, MapStorable}

class GameObjects(val dimensions: Dimensions2df) {
  def get(view: View): Set[MapStorable[Entity]] = {
    map.getNodes(view.gameX, view.gameY, view.gameWidth, view.gameHeight)
  }

  def getZombies(): mutable.MutableList[MapStorable[Zombie]] = {
    zombies.map(map.getEntityUnsafe)
  }

  assert(dimensions != null)

  val map = new Map2d[Entity](dimensions, Dimensions2df(20, 20))

  val (mapZombie) = {
    val zombie = new Zombie()
    (MapStorable(new Position2df(10.0f, 10.0f), zombie.getDimensions, zombie))
  }
  map.insert(mapZombie)

  val (mapPlayer) = {
    val player = new Player()
    (MapStorable(new Position2df(20.0f, 20.0f), player.getDimensions, player))
  }
  map.insert(mapPlayer)

  val zombies: mutable.MutableList[Zombie] = mutable.MutableList[Zombie]()
  zombies += mapZombie.item

  def addZombie(zombie: MapStorable[Zombie]): Unit = {
    if (map.inBounds(zombie)) {
      map.insert(zombie)
      zombies += zombie.item
    }
  }

  def movePlayer(vector: Vector2df): Unit = {

    val playerLoc = map.getEntityUnsafe(mapPlayer.item)
    val collidableZombieLoc = map.getEntityUnsafe(mapZombie.item)

    val newPlayerPos = playerLoc.position.incX(vector.x).incY(vector.y)
    val playerRect = new Rectangle(newPlayerPos.x, newPlayerPos.y, playerLoc.dimensions.width, playerLoc.dimensions.height)
    val zombieRect =
      new Rectangle(
        collidableZombieLoc.position.x,
        collidableZombieLoc.position.y,
        collidableZombieLoc.dimensions.width,
        collidableZombieLoc.dimensions.height
      )

    if (!playerRect.overlaps(zombieRect) && map.inBounds(playerLoc.copy(position = newPlayerPos))) {
      val newPlayerLoc = playerLoc.copy(position = playerLoc.position.add(vector))
      map.move(mapPlayer.item, newPlayerLoc)
    } else
      System.out.println("Blocked")
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

  def getPlayerPosition: Position2df = map.getEntityUnsafe(mapPlayer.item).position
}
