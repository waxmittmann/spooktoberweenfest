package mwittmann.spooktober.screen

import mwittmann.spooktober.unit.Dimensions2df

case class GameState(
  gameFactor: Float = 0.1f,
  factor: Float = 5.0f,
  playerSpeed: Float = 10,
  gameDimensions: Dimensions2df = new Dimensions2df(800, 800),
  view: View = View.emptyView
)
