package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.pipeline.{PipelineStage, State}

object ZombieStage extends PipelineStage {
  override val name = "Zombie"

  override def run(state: State): State = {
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

    // Todo: Map here, and then copy into state
    state
  }
}
