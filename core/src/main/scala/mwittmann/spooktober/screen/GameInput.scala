package mwittmann.spooktober.screen

import com.badlogic.gdx.{Gdx, Input => GdxInput}
import mwittmann.spooktober.pipeline.stages.ZoomInput.{MaintainZoom, ZoomIn, ZoomInput, ZoomOut}
import mwittmann.spooktober.pipeline.state.{InputState, ViewState}
import mwittmann.spooktober.unit.Direction._
import mwittmann.spooktober.unit.{Direction, Position2df}

object GameInput {

  def getInput(view: ViewState): InputState = {
    val movement = movementInput

    InputState(
      movement      = movement,
      isAddZombies  = Gdx.input.isKeyPressed(GdxInput.Keys.ENTER),
      zoomInput     = zoomInput,
      mouseInput    = mouseInput(view),
      firePressed   = fireInput
    )
  }

  private def fireInput: Boolean = Gdx.input.isKeyPressed(GdxInput.Keys.SPACE)

  private def mouseInput(view: ViewState): Position2df =
    Position2df(view.untranslateX(Gdx.input.getX()), view.untranslateY(Gdx.graphics.getHeight - Gdx.input.getY()))

  private def zoomInput: ZoomInput = {
    if (Gdx.input.isKeyPressed(GdxInput.Keys.MINUS)) ZoomOut
    else if (Gdx.input.isKeyPressed(GdxInput.Keys.EQUALS)) ZoomIn
    else MaintainZoom
  }

  private def movementInput = {
    val isW = Gdx.input.isKeyPressed(GdxInput.Keys.W)
    val isA = Gdx.input.isKeyPressed(GdxInput.Keys.A)
    val isD = Gdx.input.isKeyPressed(GdxInput.Keys.D)
    val isS = Gdx.input.isKeyPressed(GdxInput.Keys.S)

    val moveHoriz = if (isA && !isD) Direction.Left else if (!isA && isD) Direction.Right else Neutral
    val moveVert = if (isW && !isS) Up else if (!isW && isS) Down else Neutral

    val movement = (moveHoriz, moveVert) match {
      case (Direction.Left, Down) => LowerLeft
      case (Direction.Left, Neutral) => Direction.Left
      case (Direction.Left, Up) => UpperLeft

      case (Neutral, Down) => Down
      case (Neutral, Neutral) => Neutral
      case (Neutral, Up) => Up

      case (Direction.Right, Down) => LowerRight
      case (Direction.Right, Neutral) => Direction.Right
      case (Direction.Right, Up) => UpperRight
    }
    movement
  }
}
