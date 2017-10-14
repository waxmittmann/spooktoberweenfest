package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.pipeline.stages.ZoomInput.{MaintainZoom, ZoomInput}
import mwittmann.spooktober.unit.Direction.{Direction, Neutral}
import mwittmann.spooktober.unit.Position2df

object InputState {
  def NoInput = InputState(Neutral, false, MaintainZoom, Position2df(0, 0))
}

case class InputState(
  movement: Direction,
  isAddZombies: Boolean,
  zoomInput: ZoomInput,
  mouseInput: Position2df
)
