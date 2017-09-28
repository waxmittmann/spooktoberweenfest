package mwittmann.spooktober.screen

import scala.collection.mutable
import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.entity.{Entity, Player, Zombie}
import mwittmann.spooktober.unit.Dimensions2df
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.unit.Vector2df
import mwittmann.spooktober.util.Map2d
import mwittmann.spooktober.util.Map2dO.MapStorable

class GameObjects(val dimensions: Dimensions2df) {
  assert(dimensions != null)

  val map = new Map2d[Entity](dimensions, Dimensions2df(20, 20))

  val collidableZombie: map.MapStorable[Zombie] = {
    val zombie = new Zombie()
    map.MapStorable(new Position2df(10.0f, 10.0f), zombie.getDimensions, zombie)
  }
  map.insert(collidableZombie)

  val player = {
    val player = new Player()
    map.MapStorable(new Position2df(0.0f, 0.0f), player.getDimensions, player)
  }
  map.insert(player)

  val zombies: mutable.MutableList[Zombie] = mutable.MutableList[Zombie]()
  zombies += collidableZombie.item

  def addZombie(zombie: Zombie): Unit = zombies += zombie

  def movePlayer(vector: Vector2df): Unit = {

    val newPlayerPos = player.getPosition.incX(vector.x).incY(vector.y)
    val playerRect = new Rectangle(newPlayerPos.x, newPlayerPos.y, player.getDimensions.width, player.getDimensions.height)
    val zombieRect = new Rectangle(collidableZombie.getPosition.x, collidableZombie.getPosition.y, collidableZombie.getDimensions.width, collidableZombie.getDimensions.height)

    if (!playerRect.overlaps(zombieRect))
      player.movePosition(vector)
    else
      System.out.println("Blocked")
  }

  def moveZombies(deltaSeconds: Float): Unit = {
    for (zombie <- zombies) {
      val mv = zombie.getMove(deltaSeconds)

      zombie.movePosition(mv)

      if (zombie.getPosition.x < 0) zombie.setPosition(zombie.getPosition.withX(0))
      else if (zombie.getPosition.x > dimensions.width) zombie.setPosition(zombie.getPosition.withX(dimensions.width))

      if (zombie.getPosition.y < 0) zombie.setPosition(zombie.getPosition.withY(0))
      else if (zombie.getPosition.y > dimensions.height) zombie.setPosition(zombie.getPosition.withY(dimensions.height))
    }
  }

  def getPlayerPosition: Position2df = player.getPosition
}
