package mwittmann.spooktober.util

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import mwittmann.spooktober.screen.GameObjects


object DebugDraw {
  private[util] val sr = new ShapeRenderer

  def point(x: Float, y: Float, radius: Int): Unit = {
    sr.setColor(1f, 0f, 0f, 1)
    sr.begin(ShapeRenderer.ShapeType.Filled)
    sr.circle(x, y, radius)
    sr.end()
  }
}

class DebugDraw {
  private[util] var stateTime: Float = 0
  private[util] var counter: Int = 0

  def tick(delta: Float, gameObjects: GameObjects): Unit = {
    if (counter >= 100) {
      counter = 0
      System.out.println("At delta, " + delta + ", " + stateTime)
      System.out.println("Player at: " + gameObjects.getPlayerPosition)
    }
    else counter += 1
    stateTime += delta
  }
}
