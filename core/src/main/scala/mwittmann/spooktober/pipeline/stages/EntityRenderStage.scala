package mwittmann.spooktober.pipeline.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import mwittmann.spooktober.entity.{Entity, GameToken}
import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.{ProjectilesState, State, ViewState}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.{DebugDraw, Map2d, MapStorable}

class EntityRenderStage extends PipelineStage {
  private val sr = new ShapeRenderer

  override def run(state: State): State = {
    import state._
    import state.Implicits._batch

    DebugDraw.point(view.screenWidth / 2, view.screenHeight / 2, 10)
    renderCursor(state.view, state.input.mouseInput)(state.batch)

    renderProjectiles(state.map, state.view, state.projectiles)
    batch.begin()
    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))

    renderMap(state.map, state.view, state.renderables())
    batch.end()
    state
  }

  // Todo: Use Libgdx cursor
  private def renderCursor(
    view: ViewState,
    mouseInput: Position2df
  )(implicit batch: SpriteBatch): Unit =
    DebugDraw.point(view.translateX(mouseInput.x), view.translateY(mouseInput.y), 5)

  def renderProjectiles(
    map: Map2d[GameToken],
    view: ViewState,
    projectiles: ProjectilesState
  ): Unit = {
    sr.setProjectionMatrix(sr.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))
    sr.setColor(1f, 0f, 0f, 1)
    sr.begin(ShapeRenderer.ShapeType.Filled)
    projectiles.projectiles.foreach { projectile =>
      sr.circle(view.translateX(projectile.position.x), view.translateY(projectile.position.y), 8)
    }
    sr.end()
  }


  private def renderMap(
    map: Map2d[GameToken],
    view: ViewState,
    entitiesToRender: List[Entity]
  )(implicit batch: SpriteBatch): Unit = {
    val objectsToRender: Set[MapStorable[GameToken]] = map.getNodes(view)

    val tokenToTexture = entitiesToRender.map(e => (e.token, e)).toMap

    for (renderable <- objectsToRender) {
      render(view, tokenToTexture, renderable)
    }
  }

  private def render(
    view: ViewState,
    tokenToEntity: Map[GameToken, Entity],
    renderable: MapStorable[GameToken]
  )(implicit batch: SpriteBatch): Unit = {
    val texture = tokenToEntity(renderable.item).texture

    val x = view.translateX(renderable.position.x)
    val y = view.translateY(renderable.position.y)

    val width = view.translateWidth(renderable.dimensions.width)
    val height = view.translateHeight(renderable.dimensions.height)

    batch.draw(texture, x, y, width/2.0f, height/2.0f, width, height, 1, 1, renderable.rotation)
  }
}
