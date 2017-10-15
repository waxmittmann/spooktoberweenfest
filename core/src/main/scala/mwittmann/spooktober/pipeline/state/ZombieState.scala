package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.entity._
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{GlobalRandom, Map2d, MapStorable}

case class ZombieState(zombies: List[Zombie] = List.empty) {

  def add(position2df: Position2df)(implicit map: Map2d[GameToken]): ZombieState = {
    val zombie = Zombie(
      animation = if (GlobalRandom.random.nextInt(2) == 0) Assets.zombieA else Assets.zombieB
    )

    val insertSuccess =
      map.insertIfPossible(
        ZombieStorable(position2df, zombie.zombieAttributes.dimensions, GlobalRandom.random.nextFloat() * 360, zombie.token)
      )

    if (insertSuccess) this.copy(zombies = zombies :+ zombie)
    else this
  }

  def removeMany(hitZombies: Set[ZombieToken])(implicit map: Map2d[GameToken]): ZombieState = {
    // Todo: Ugh, that z => z : Entity bit... gotta figure out how to do this properly
    val removedZombies = map.removeMany(hitZombies.map(z => z: GameToken))
    assert(removedZombies == hitZombies, s"Didn't delete all the zombies! Removed:\n$removedZombies\nHit:\n$hitZombies")
    this.copy(zombies = zombies.filterNot(z => hitZombies.contains(z.token)))
  }

}
