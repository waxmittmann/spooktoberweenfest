package mwittmann.spooktober.screen

import scala.collection.mutable
import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.unit.Dimensions2df
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.unit.Vector2df
import mwittmann.spooktober.util.{DebugLog, Map2d, MapStorable}

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
    if (map.inBounds(zombie) && map.checkCollision(zombie).isEmpty
    ) {
      map.insert(zombie)
      zombies += zombie.item
    }
  }

  def moveZombies(view: View, deltaSeconds: Float): Unit = {
    // Todo: Figure out how to get only what I want out
    val inView = map.getNodes(view)
    println(s"Figuring out zombies, ${inView.size} in view")
    for (storable <- inView) {
      storable.item match {
        case zombie: Zombie => {
          println(s"Moving $zombie")
          //for (zombie <- zombies) {
          val move = zombie.getMove(deltaSeconds)
          //val mapZombie = map.getEntityUnsafe(zombie)
          val newZombiePos = storable.position.add(move)

          val newMapZombie = storable.copy(position = newZombiePos)

          if (
            map.inBounds(newZombiePos, zombie.dimensions) &&
              map.checkCollision(newMapZombie).isEmpty
          ) {
            map.move(zombie, newZombiePos)
          }
        }

        case _ => ()
      }
    }
  }

  def getPlayerPosition: Position2df = map.getEntityUnsafe(player).position

  def getPlayerDimensions = player.getDimensions

  def movePlayer(view: View, vector: Vector2df): Unit = {
    val playerLoc = map.getEntityUnsafe(player)
    val newPlayerPos = playerLoc.position.incX(vector.x).incY(vector.y)
    val newPlayerLoc = playerLoc.copy(position = newPlayerPos)

    if (map.inBounds(newPlayerLoc) && map.checkCollision(newPlayerLoc).isEmpty) {
      map.move(player, newPlayerLoc)
    } else {
      DebugLog.dprintln("Blocked")
    }
  }
}
