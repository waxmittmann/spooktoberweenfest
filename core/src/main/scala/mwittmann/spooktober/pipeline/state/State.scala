package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.entity.Entity
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.Map2d

case class State(
  zombies: ZombieState = new ZombieState(),
  player: PlayerState,
  map: Map2d[Entity],
  view: ViewState = ViewState.emptyView,
  input: InputState = InputState.NoInput,
  delta: Float = 0.0f,
  gameFactor: Float = 0.1f,
  factor: Float = 5.0f,
  playerSpeed: Float = 10,
  gameDimensions: Dimensions2df,
) {
  implicit val imap = map
}

object State {
  def apply(gameDimensions: Dimensions2df): State = {
    val map = new Map2d[Entity](gameDimensions, Dimensions2df(20, 20))
    val playerState = PlayerState(map, Position2df(50, 50))

    State(
      player = playerState,
      gameDimensions = gameDimensions,
      map = map
    )
  }
}