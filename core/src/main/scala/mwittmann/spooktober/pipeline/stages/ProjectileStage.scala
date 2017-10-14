package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.State

object ProjectileStage extends PipelineStage {
  override def run(state: State) = {
    state.copy(
      projectiles = state.projectiles.update(state.gameDimensions, state.delta)
    )
  }
}
