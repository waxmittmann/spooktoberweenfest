package mwittmann.spooktober.pipeline.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{State, TerrainState, ViewState}

class TerrainRenderStage extends PipelineStage {

  override def run(state: State): State = {
    import state._
    import state.Implicits._batch

    batch.begin()
    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))
    drawTerrain(state.view, state.terrainState)
    batch.end()
    state
  }

  def drawTerrain(view: ViewState, terrainState: TerrainState)(implicit batch: SpriteBatch) = {
    val width = view.translateWidth(terrainState.tileSize)
    val height = view.translateHeight(terrainState.tileSize)

    for {
      x <- terrainState.tiles.indices
      y <- terrainState.tiles(0).indices
    } yield {
      val terrainType = terrainState.tiles(x)(y)

      val xTrans = view.translateX(x * terrainState.tileSize)
      val yTrans = view.translateY(y * terrainState.tileSize)

      batch.draw(
        terrainState.terrainTextures(terrainType),
        xTrans, yTrans,
        width, height
      )
    }
  }
}
