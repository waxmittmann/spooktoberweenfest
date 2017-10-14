package mwittmann.spooktober.pipeline

import mwittmann.spooktober.pipeline.stages._
import mwittmann.spooktober.pipeline.state.State

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
    UIStage,
    PlayerStage,
    ZombieStage,
    new TerrainRenderStage(),
    new EntityRenderStage()
  ))
}