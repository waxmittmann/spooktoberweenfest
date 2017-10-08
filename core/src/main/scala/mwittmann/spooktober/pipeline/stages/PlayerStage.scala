package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.pipeline._
import mwittmann.spooktober.pipeline.state.State
import mwittmann.spooktober.unit.{Direction, Vector2df}
import mwittmann.spooktober.unit.Direction._

object PlayerStage extends PipelineStage {
  override val name = "Player"

  override def run(state: State): State = {
    import state._

    val movementAmount: Float = playerSpeed * delta * factor
    val moveVector = state.input.movement match {
      case LowerLeft        => Vector2df(-movementAmount, -movementAmount)
      case Direction.Left   => Vector2df(-movementAmount, 0)
      case UpperLeft        => Vector2df(-movementAmount, movementAmount)
      case Down             => Vector2df(0, -movementAmount)
      case Neutral          => Vector2df(0, 0)
      case Up               => Vector2df(0, movementAmount)
      case LowerRight       => Vector2df(movementAmount, -movementAmount)
      case Direction.Right  => Vector2df(movementAmount, 0)
      case UpperRight       => Vector2df(movementAmount, movementAmount)
    }

    movePlayer(state, moveVector)
    state
  }

  def movePlayer(
    state: State,
    moveVector: Vector2df
  ): Boolean = {
    import state.imap
    val newPlayerPos = state.player.getPosition.incX(moveVector.x).incY(moveVector.y)
    state.player.move(newPlayerPos)
  }
}
