package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.entity.{GameToken, Zombie, ZombieStorable, ZombieToken}
import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{Projectile, State, ZombieState}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{GlobalRandom, MapStorable}

object ZombieStage extends PipelineStage {

  override def run(state: State): State = {
    val s1 = addZombies(state)
    val s2 = hitZombies(s1)
    val s3 = s2.copy(zombies = runZombieAI(s2.delta, s2.zombies))
    renderZombies(s2)
    s3
  }

  def runZombieAI(delta: Float, state: ZombieState): ZombieState = {
    state.copy(zombies =
      state.zombies.map((zombie: Zombie) => {
        zombie.copy(
          stateTime = zombie.stateTime + delta, // Bit dodgy to do this here
          zombieAttributes = zombie.zombieAI.update(delta, zombie.zombieAttributes)
        )
      })
    )
  }

  def hitZombies(state: State): State = {
    val hitZombies = state.projectiles.projectiles.flatMap { (projectile: Projectile) =>
      state.map
        .getNodes(projectile.centerPosition, projectile.dimensions)
        .collect { case zs @ ZombieStorable(_, _, _, _) => zs }
        .filter(zombie => zombie.asRectangle.contains(projectile.toRectangle))
        .map(_.item)
    }.toSet

    // Todo: Add corpse
    state.copy(zombies = state.zombies.removeMany(hitZombies)(state.map))
  }

  def renderZombies(state: State): Unit = {
    import state._

    // Todo: Don't do this everywhere; have in state
    val tokenToZombie: Map[GameToken, Zombie] = zombies.zombies.map(z => (z.token, z)).toMap

    // Todo: Figure out how to get only what I want out
    // Todo: Now I might not notice if I don't render all zombies?
    val inView: Set[(MapStorable[GameToken], Zombie)] = map.getNodes(view).flatMap(ms => tokenToZombie.get(ms.item).map(zombie => (ms, zombie)))

    // Todo: Should have entities in view globally available in State somewhere
    inView.foreach { zombie =>
      val (move, angle) = zombie._2.zombieAttributes.currentDirection
      val newZombiePos = zombie._1.position.add(move.mult(state.delta))
      val newMapZombie = zombie._1.copy(position = newZombiePos, rotation = angle)
      if (map.inBounds(newZombiePos, zombie._2.zombieAttributes.dimensions) && map.checkCollision(newMapZombie).isEmpty)
        map.move(zombie._1.item, newZombiePos, Some(angle))
    }
  }


  def addZombies(state: State): State = {
    import state._
    import state.Implicits._map

    if (state.input.isAddZombies) {
      val newZombieState =
        (0 to 100).foldLeft(state.zombies)((zombies, _) => {
          val x = GlobalRandom.random.nextInt(gameDimensions.width.toInt)
          val y = GlobalRandom.random.nextInt(gameDimensions.height.toInt)
          zombies.add(Position2df(x, y))
        })

      state.copy(zombies = newZombieState)
    } else state
  }
}
