package mwittmann.spooktober.pipeline.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{State, TerrainState, ViewState}

class TerrainRenderStage extends PipelineStage {
  override val name = "Terrain"

  private val batch = new SpriteBatch

  override def run(state: State): State = {
    import state._

    batch.begin()
    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))
    drawTerrain(state.view, state.terrainState)
    batch.end()
    state
  }

  def drawTerrain(view: ViewState, terrainState: TerrainState) = {
    val width = view.translateWidth(terrainState.tileSize)
    val height = view.translateHeight(terrainState.tileSize)

    for {
      x <- terrainState.tiles.indices
      y <- terrainState.tiles(0).indices
//      x <- List()
//      y <- 1 to 1
//      _ <- 0 to 1
//      x = 0
//      y = 0
//      xy <- List((0, 0), (0, 1), (1, 1))
    } yield {
//      val (x, y) = (xy._1, xy._2)
      val terrainType = terrainState.tiles(x)(y)

      //println(s"Height: $height, Width: $width")

      val xTrans = view.translateX(x * terrainState.tileSize)
      val yTrans = view.translateY(y * terrainState.tileSize)

      batch.draw(
        terrainState.terrainTextures(terrainType),
        xTrans, yTrans,
        width, height
      )
    }
  }

  override def cleanup(): Unit = {
    batch.dispose()
  }
}
