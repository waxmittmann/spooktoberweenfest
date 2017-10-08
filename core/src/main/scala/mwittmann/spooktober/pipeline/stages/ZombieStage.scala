package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.pipeline.{PipelineStage, State}
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
          val move = zombie.getMove(delta)
          val newZombiePos = storable.position.add(move)

          val newMapZombie = storable.copy(position = newZombiePos)

          if (map.inBounds(newZombiePos, zombie.dimensions) && map.checkCollision(newMapZombie).isEmpty)
            map.move(zombie, newZombiePos)
        }

        case _ => ()
      }
    }
  }


  def addZombies(state: State): Unit = {
    import state._

    if (state.input.isAddZombies) {
      for { _ <- 0 to 200 } yield {
        val x = GlobalRandom.random.nextInt(gameDimensions.width.toInt)
        val y = GlobalRandom.random.nextInt(gameDimensions.height.toInt)
        state.zombies.add(Position2df(x, y))
      }
    }
  }


}
