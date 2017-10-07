package mwittmann.spooktober.pipeline

import mwittmann.spooktober.pipeline.stages.{PlayerStage, RenderStage, ZombieStage}

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


object Input {
  def NoInput = Input(Neutral)
}

case class Input(movement: Direction)

trait PipelineStage {
  val name: String

  def run(state: State): State

  def cleanup: Unit = ()
}


class Pipeline(stages: Seq[PipelineStage]) {
  def run(state: State): State =
    stages.foldLeft(state)((state, stage) => stage.run(state))

  def debug(state: State): Seq[(String, State)] =
    stages.scanLeft(("initial", state))((state, stage) => (stage.name, stage.run(state._2)))

  def cleanup: Unit = stages.foreach(_.cleanup)
}

object Pipeline {
  def standardPipeline = new Pipeline(Seq(
    PlayerStage,
    ZombieStage,
    new RenderStage()
  ))
}