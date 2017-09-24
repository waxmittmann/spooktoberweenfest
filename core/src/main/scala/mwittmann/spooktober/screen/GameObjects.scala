package mwittmann.spooktober.screen

import com.badlogic.gdx.math.Rectangle
import javafx.geometry.Pos

import mwittmann.spooktober.entity.Player
import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.unit.Dimensions2d
import mwittmann.spooktober.unit.Position
import mwittmann.spooktober.unit.Vector2df

import scala.collection.mutable


class GameObjects(val dimensions: Dimensions2d) {
  assert(dimensions != null)

  val collidableZombie = new Zombie(new Position(dimensions.x / 2 + 50, dimensions.y / 2 + 50))
  val player = new Player(new Position(dimensions.x / 2, dimensions.y / 2))

  val zombies: mutable.MutableList[Zombie] = mutable.MutableList[Zombie]()

  zombies.+=(collidableZombie)

  def addZombie(zombie: Zombie): Unit = {
    zombies.+=(zombie)
  }

  def movePlayer(vector: Vector2df): Unit = {
    val newPlayerPos = player.getPosition.incX(vector.x).incY(vector.y)
    val playerRect = new Rectangle(newPlayerPos.x, newPlayerPos.y, player.getDimensions.x, player.getDimensions.y)
    val zombieRect = new Rectangle(collidableZombie.getPosition.x, collidableZombie.getPosition.y, collidableZombie.getDimensions.x, collidableZombie.getDimensions.y)
    if (!playerRect.overlaps(zombieRect)) player.movePosition(vector)
    else System.out.println("Blocked")
  }

  def moveZombies(deltaSeconds: Float): Unit = {
    for (zombie <- zombies) {
      val mv = zombie.getMove(deltaSeconds)
      zombie.movePosition(mv)
      if (zombie.getPosition.x < 0) zombie.setPosition(zombie.getPosition.withX(0))
      else if (zombie.getPosition.x > dimensions.x) zombie.setPosition(zombie.getPosition.withX(dimensions.x))
      if (zombie.getPosition.y < 0) zombie.setPosition(zombie.getPosition.withY(0))
      else if (zombie.getPosition.y > dimensions.y) zombie.setPosition(zombie.getPosition.withY(dimensions.y))
    }
  }

  def getPlayerPosition: Position = player.getPosition
}
