package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{State, ZombieState}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.GlobalRandom

object ZombieStage extends PipelineStage {
  override val name = "Zombie"

  override def run(state: State): State = {
    addZombies(state)
    renderZombies(state)
    state
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
