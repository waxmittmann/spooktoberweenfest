package mwittmann.spooktober.screen

import scala.collection.mutable
import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.unit.Dimensions2df
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.unit.Vector2df
import mwittmann.spooktober.util.Map2d

class GameObjects(val dimensions: Dimensions2df) {
  assert(dimensions != null)

  val map = new Map2d[Entity](dimensions, Dimensions2df(20, 20))

  val (collidableZombie, mapZombie) = {
    val zombie = new Zombie()
    (zombie, map.MapStorable(new Position2df(10.0f, 10.0f), zombie.getDimensions, zombie))
  }
  map.insert(mapZombie)

  val (player, mapPlayer) = {
    val player = new Player()
    (player, map.MapStorable(new Position2df(0.0f, 0.0f), player.getDimensions, player))
  }
  map.insert(mapPlayer)

  val zombies: mutable.MutableList[Zombie] = mutable.MutableList[Zombie]()
//  zombies += collidableZombie.item
  zombies += collidableZombie

  def addZombie(zombie: Zombie): Unit = zombies += zombie

  def movePlayer(vector: Vector2df): Unit = {

    val playerLoc = map.getEntityUnsafe(player)
    val collidableZombieLoc = map.getEntityUnsafe(collidableZombie)

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
      map.move(player, newPlayerLoc)
    } else
      System.out.println("Blocked")
  }

  // Todo
  def moveZombies(deltaSeconds: Float): Unit = {
//    for (zombie <- zombies) {
//      val mv = zombie.getMove(deltaSeconds)
//
//      zombie.movePosition(mv)
//
//      if (zombie.getPosition.x < 0) zombie.setPosition(zombie.getPosition.withX(0))
//      else if (zombie.getPosition.x > dimensions.width) zombie.setPosition(zombie.getPosition.withX(dimensions.width))
//
//      if (zombie.getPosition.y < 0) zombie.setPosition(zombie.getPosition.withY(0))
//      else if (zombie.getPosition.y > dimensions.height) zombie.setPosition(zombie.getPosition.withY(dimensions.height))
//    }
  }

  def getPlayerPosition: Position2df = map.getEntityUnsafe(player).position
}
