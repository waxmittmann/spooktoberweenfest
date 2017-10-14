package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.stages.ZoomInput.{MaintainZoom, ZoomIn, ZoomOut}
import mwittmann.spooktober.pipeline.state.State

object ZoomInput {
  sealed trait ZoomInput
  case object ZoomOut extends ZoomInput
  case object ZoomIn extends ZoomInput
  case object MaintainZoom extends ZoomInput
}

object InputActionsStage extends PipelineStage {

  override def run(state: State) = {
    val newGameFactor =
      state.input.zoomInput match {
        case ZoomOut      =>
          val newGameFactor = state.gameFactor * (1 + 0.5f * state.delta)
          if (newGameFactor > 10.0f)
            10.0f
          else
            newGameFactor

        case ZoomIn       =>
          val newGameFactor = state.gameFactor / (1 + 0.5f * state.delta)
          if (newGameFactor < 0.01)
            0.01f
          else
            newGameFactor

        case MaintainZoom => state.gameFactor
      }

    state.copy(gameFactor = newGameFactor)
  }
}
