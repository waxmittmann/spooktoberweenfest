package mwittmann.spooktober.pipeline.stages

import mwittmann.spooktober.entity.{Entity, Player}
import mwittmann.spooktober.pipeline
import mwittmann.spooktober.pipeline._
import mwittmann.spooktober.screen.View
import mwittmann.spooktober.unit.Vector2df
import mwittmann.spooktober.util.{DebugLog, Map2d}

object PlayerStage extends PipelineStage {
  override val name = "Player"

  override def run(state: State): State = {
    import state._

    val movementAmount = playerSpeed * delta * factor
    val moveVector = state.input.movement match {
      case LowerLeft  => Vector2df(-movementAmount, -movementAmount)
      case pipeline.Left       => Vector2df(-movementAmount, 0)
      case UpperLeft  => Vector2df(-movementAmount, movementAmount)
      case Neutral    => Vector2df(0, 0)
      case LowerRight => Vector2df(movementAmount, -movementAmount)
      case pipeline.Right      => Vector2df(movementAmount, 0)
      case UpperRight => Vector2df(movementAmount, movementAmount)
    }

    movePlayer(state, moveVector)
    state
    //state.copy(map = newMap)
  }

  def movePlayer(
    state: State,
    moveVector: Vector2df
  ): Boolean = {
    import state.imap

//    val playerLoc = map.getEntityUnsafe(player)
    val newPlayerPos = state.player.getPosition.incX(moveVector.x).incY(moveVector.y)
//    val newPlayerLoc = playerLoc.copy(position = newPlayerPos)

    // Todo: Map!
//    if (playerState.isValidLoc(newPlayerLoc)) {
//      playerState.move(newPlayerLoc)
//    }

    state.player.move(newPlayerPos)


//    if (map.inBounds(newPlayerLoc) && map.checkCollision(newPlayerLoc).isEmpty) {
//      map.move(player, newPlayerLoc)
//    } else {
//      DebugLog.dprintln("Blocked")
//    }
//    map
  }
}
