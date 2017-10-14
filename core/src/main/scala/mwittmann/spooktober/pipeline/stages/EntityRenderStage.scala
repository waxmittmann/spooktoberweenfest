package mwittmann.spooktober.pipeline.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import mwittmann.spooktober.entity.Entity
import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{State, ViewState}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{DebugDraw, Map2d, MapStorable}

class EntityRenderStage extends PipelineStage {

  override def run(state: State): State = {
    import state._
    import state.Implicits._batch

    DebugDraw.point(view.screenWidth / 2, view.screenHeight / 2, 10)
    renderCursor(state.view, state.input.mouseInput)(state.batch)

    batch.begin()
    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))
    renderMap(state.map, state.view)
    batch.end()
    state
  }

  // Todo: Use Libgdx cursor
  private def renderCursor(
    view: ViewState,
    mouseInput: Position2df
  )(implicit batch: SpriteBatch): Unit =
    DebugDraw.point(view.translateX(mouseInput.x), view.translateY(mouseInput.y), 5)

  private def renderMap(map: Map2d[Entity], view: ViewState)(implicit batch: SpriteBatch): Unit = {
    val objectsToRender = map.getNodes(view)
    for (renderable <- objectsToRender) {
      render(view, renderable)
    }
  }

  private def render(view: ViewState, renderable: MapStorable[Entity])(implicit batch: SpriteBatch): Unit = {
    val texture = renderable.item.texture

    val x = view.translateX(renderable.position.x)
    val y = view.translateY(renderable.position.y)

    val width = view.translateWidth(renderable.dimensions.width)
    val height = view.translateHeight(renderable.dimensions.height)

    batch.draw(texture, x, y, width/2.0f, height/2.0f, width, height, 1, 1, renderable.rotation)
  }
}
