package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.pipeline.stages.ZoomInput.{MaintainZoom, ZoomInput}
import mwittmann.spooktober.unit.Direction.{Direction, Neutral}

object InputState {
  def NoInput = InputState(Neutral, false, MaintainZoom)
}

case class InputState(
  movement: Direction,
  isAddZombies: Boolean,
  zoomInput: ZoomInput
)
