package mwittmann.spooktober.util

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import mwittmann.spooktober.screen.GameObjects

object DebugDraw {
  private val sr = new ShapeRenderer

  def point(x: Float, y: Float, radius: Int): Unit = {
    sr.setColor(1f, 0f, 0f, 1)
    sr.begin(ShapeRenderer.ShapeType.Filled)
    sr.circle(x, y, radius)
    sr.end()
  }
}