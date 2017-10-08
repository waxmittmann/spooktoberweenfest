package mwittmann.spooktober.pipeline

import mwittmann.spooktober.pipeline.stages.{PlayerStage, RenderStage, ZombieStage}
import mwittmann.spooktober.unit.Direction._

object Input {
  def NoInput = Input(Neutral, false)
}

case class Input(movement: Direction, isAddZombies: Boolean)

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