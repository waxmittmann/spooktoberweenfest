package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.entity.{Entity, Zombie}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{Map2d, MapStorable}

case class ZombieState(zombies: List[Zombie] = List.empty) {
  def add(position2df: Position2df)(implicit map: Map2d[Entity]): ZombieState = {
    val zombie = new Zombie()
    map.insertIfPossible(MapStorable(position2df, zombie.getDimensions, zombie))
    this.copy(zombies = zombies :+ zombie)
  }
}
