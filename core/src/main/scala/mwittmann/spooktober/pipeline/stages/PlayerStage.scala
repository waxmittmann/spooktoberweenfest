package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.pipeline._
import mwittmann.spooktober.pipeline.state.{ProjectilesState, State}
import mwittmann.spooktober.unit.{Direction, Position2df, Vector2df}
import mwittmann.spooktober.unit.Direction._
import mwittmann.spooktober.util.MathUtils

object PlayerStage extends PipelineStage {

  val timeBetweenShots = 0.45f

  def doAttack(
    projectileState: ProjectilesState,
    playerPosition: Position2df,
    playerDirection: Vector2df
  ): ProjectilesState =
    projectileState.add(playerPosition, playerDirection, 90)

  override def run(state: State): State = {
    doMovement(state)
    doRotation(state)
    if (state.input.firePressed && state.player.lastFiredAgo >= timeBetweenShots) {
      val mouseAt = state.input.mouseInput
      val playerAt = state.player.getCenterPosition(state.map)

      val directionVector = Vector2df(
        mouseAt.x - playerAt.x,
        mouseAt.y - playerAt.y
      ).normalize

      state.copy(
        projectiles =
          doAttack(
            state.projectiles,
            playerAt,
            directionVector
          ),

        player = state.player.copy(lastFiredAgo = 0)
      )
    } else {
      state.copy(player = state.player.copy(lastFiredAgo = state.player.lastFiredAgo + state.delta))
    }
  }

  private def doRotation(state: State): Unit = {
    val mouseAt = state.input.mouseInput
    val playerAt = state.player.getCenterPosition(state.map)

    val angleTo = Position2df(
      mouseAt.x - playerAt.x,
      mouseAt.y - playerAt.y
    )

    val newAngle = MathUtils.getAngle2(Vector2df(0, 1), angleTo)
    state.player.setRotation(newAngle)(state.map)
  }

  private def doMovement(state: State) = {
    import state._
    val movementAmount = playerSpeed * delta * factor
    val moveVector = state.input.movement match {
      case LowerLeft => Vector2df(-movementAmount, -movementAmount)
      case Direction.Left => Vector2df(-movementAmount, 0)
      case UpperLeft => Vector2df(-movementAmount, movementAmount)
      case Down => Vector2df(0, -movementAmount)
      case Neutral => Vector2df(0, 0)
      case Up => Vector2df(0, movementAmount)
      case LowerRight => Vector2df(movementAmount, -movementAmount)
      case Direction.Right => Vector2df(movementAmount, 0)
      case UpperRight => Vector2df(movementAmount, movementAmount)
    }

    movePlayer(state, moveVector)
  }

  private def movePlayer(
    state: State,
    moveVector: Vector2df
  ): Boolean = {
    import state.Implicits._map

    val newPlayerPos = state.player.getPosition.incX(moveVector.x).incY(moveVector.y)
    state.player.move(newPlayerPos)
  }
}
