package mwittmann.spooktober.screen

import mwittmann.spooktober.unit.Dimensions2d

case class GameState(
  gameFactor: Float = 0.1f,
  factor: Float = 5.0f,
  playerSpeed: Float = 10,
  gameDimensions: Dimensions2d = new Dimensions2d(800, 800)
)
