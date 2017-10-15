package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.entity.{Zombie, ZombieStorable}
import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{Projectile, State, ZombieState}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.GlobalRandom

object ZombieStage extends PipelineStage {

  override def run(state: State): State = {
    addZombies(state)
    val newState = hitZombies(state)
    renderZombies(state)
    newState
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

  def renderZombies(state: State) = {
    import state._

    // Todo: Figure out how to get only what I want out
    val inView = map.getNodes(view)
    for (storable <- inView) {
      storable.item match {
        case zombie: Zombie => {
          val (move, angle) = zombie.getMove(delta)
          val newZombiePos = storable.position.add(move)
          val newMapZombie = storable.copy(position = newZombiePos, rotation = angle)
          if (map.inBounds(newZombiePos, zombie.dimensions) && map.checkCollision(newMapZombie).isEmpty)
            map.move(zombie, newZombiePos, Some(angle))
        }

        case _ => ()
      }
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
