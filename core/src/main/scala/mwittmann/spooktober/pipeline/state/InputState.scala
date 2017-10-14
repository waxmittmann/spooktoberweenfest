package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.pipeline.stages.ZoomInput.{MaintainZoom, ZoomInput}
import mwittmann.spooktober.unit.Direction.{Direction, Neutral}
import mwittmann.spooktober.unit.Position2df

object InputState {
  def NoInput = InputState(
    movement = Neutral,
    isAddZombies = false,
    zoomInput = MaintainZoom,
    mouseInput = Position2df(0, 0),
    firePressed = false
  )
}

case class InputState(
  movement: Direction,
  isAddZombies: Boolean,
  zoomInput: ZoomInput,
  mouseInput: Position2df,
  firePressed: Boolean
)
