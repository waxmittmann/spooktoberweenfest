package mwittmann.spooktober.unit

object Direction {
  sealed trait Direction
  case object LowerLeft extends Direction
  case object Left extends Direction
  case object UpperLeft extends Direction
  case object Neutral extends Direction
  case object LowerRight extends Direction
  case object Right extends Direction
  case object UpperRight extends Direction
  case object Up extends Direction
  case object Down extends Direction
}
