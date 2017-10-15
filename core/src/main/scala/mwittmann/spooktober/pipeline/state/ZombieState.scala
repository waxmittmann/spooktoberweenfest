package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.entity.{Entity, Zombie, ZombieStorable}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{GlobalRandom, Map2d, MapStorable}

case class ZombieState(zombies: List[Zombie] = List.empty) {

  def add(position2df: Position2df)(implicit map: Map2d[Entity]): ZombieState = {
    val zombie = new Zombie()
    map.insertIfPossible(ZombieStorable(position2df, zombie.getDimensions, GlobalRandom.random.nextFloat() * 360, zombie))
    this.copy(zombies = zombies :+ zombie)
  }

  def removeMany(hitZombies: Set[Zombie])(implicit map: Map2d[Entity]): ZombieState = {
    // Todo: Ugh, that z => z : Entity bit... gotta figure out how to do this properly
    val removedZombies = map.removeMany(hitZombies.map(z => z: Entity))
    assert(removedZombies == hitZombies, s"Didn't delete all the zombies! Removed:\n$removedZombies\nHit:\n$hitZombies")
    this.copy(zombies = zombies.filterNot(hitZombies.contains))
  }

}
