package mwittmann.spooktober.pipeline.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import mwittmann.spooktober.entity.Entity
import mwittmann.spooktober.pipeline.{PipelineStage, State}
import mwittmann.spooktober.screen.View
import mwittmann.spooktober.util.{DebugDraw, MapStorable}

class RenderStage extends PipelineStage {
  override val name = "Render"

  private val batch = new SpriteBatch

  override def run(state: State): State = {
    import state._

    DebugDraw.point(view.screenWidth / 2, view.screenHeight / 2, 10)

    batch.begin()
    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))

    val objectsToRender = map.getNodes(view)

    for (renderable <- objectsToRender) {
      render(view, renderable)
    }

    batch.end()
    state
  }

  def render(view: View, renderable: MapStorable[Entity]) = {
    val texture = renderable.item.texture

    val x = view.translateX(renderable.position.x)
    val y = view.translateY(renderable.position.y)

    val width = view.translateWidth(renderable.dimensions.width)
    val height = view.translateHeight(renderable.dimensions.height)
    batch.draw(texture, x, y, width, height)
  }

  override def cleanup(): Unit = {
    batch.dispose()
  }
}