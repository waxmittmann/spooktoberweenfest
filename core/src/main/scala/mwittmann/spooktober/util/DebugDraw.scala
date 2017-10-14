package mwittmann.spooktober.util

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import mwittmann.spooktober.pipeline.state.ViewState

object DebugDraw {
  private val sr = new ShapeRenderer

  def point(x: Float, y: Float, radius: Int): Unit = {
    sr.setColor(1f, 0f, 0f, 1)
    sr.begin(ShapeRenderer.ShapeType.Filled)
    sr.circle(x, y, radius)
    sr.end()
  }

  def tPoint(view: ViewState, x: Float, y: Float, radius: Int): Unit =
    point(view.translateX(x), view.translateY(y), radius)
}
