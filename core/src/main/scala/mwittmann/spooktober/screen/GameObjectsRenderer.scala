//package mwittmann.spooktober.screen
//
//import com.badlogic.gdx.Gdx
//import com.badlogic.gdx.graphics.g2d.SpriteBatch
//import mwittmann.spooktober.entity.Entity
//import mwittmann.spooktober.util.{DebugDraw, MapStorable}
//
//
//class GameObjectsRenderer {
//  private[screen] val batch = new SpriteBatch
//
//  def render(gameObjects: GameObjects, view: View): Unit = {
//    DebugDraw.point(view.screenWidth / 2, view.screenHeight / 2, 10)
//
//    batch.begin()
//    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))
//
//    val objectsToRender = gameObjects.getEntities(view)
//    for (renderable <- objectsToRender) {
//      render(view, renderable)
//    }
//
//    batch.end()
//  }
//
//  def render(view: View, renderable: MapStorable[Entity]) = {
//    val texture = renderable.item.texture
//
//    val x = view.translateX(renderable.position.x)
//    val y = view.translateY(renderable.position.y)
//
//    // Todo: fix
//    val width = view.translateWidth(renderable.dimensions.width)
//    val height = view.translateHeight(renderable.dimensions.height)
//    batch.draw(texture, x, y, width, height)
//  }
//
//  def dispose(): Unit = {
//    batch.dispose()
//  }
//}
