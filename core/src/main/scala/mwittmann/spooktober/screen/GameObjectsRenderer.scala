package mwittmann.spooktober.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureRegion}
import mwittmann.spooktober.screen.View
import mwittmann.spooktober.asset.Animation
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.entity.{Player, Zombie}
import mwittmann.spooktober.unit.Position2df
import mwittmann.spooktober.util.DebugDraw


class GameObjectsRenderer {
  private[screen] val batch = new SpriteBatch

  def render(gameObjects: GameObjects, view: View): Unit = {
    DebugDraw.point(view.screenWidth / 2, view.screenHeight / 2, 10)

    batch.begin()
    batch.setProjectionMatrix(batch.getProjectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight))

//    for (zombie <- gameObjects.zombies) {
//      renderZombie(zombie, view)
//    }

    renderPlayer(gameObjects.player, gameObjects.getPlayerPosition, view)
    batch.end()
  }

//  def renderZombie(zombie: Zombie, view: View): Unit = {
//    var animation: Animation = null
//
//    if (zombie.`type` == 0) animation = Assets.zombieA
//    else animation = Assets.zombieB
//
//    val x = view.translateX(zombie.getPosition.x)
//    val y = view.translateY(zombie.getPosition.y)
//    val width = view.translateWidth(zombie.getDimensions.width)
//    val height = view.translateHeight(zombie.getDimensions.height)
//    val frame = animation.getKeyFrame(zombie.stateTime, Animation.ANIMATION_LOOPING)
//    batch.draw(frame, x, y, width, height)
//  }

  def renderPlayer(player: Player, position: Position2df, view: View): Unit = {
    val playerTexture = Assets.player
    val x = view.translateX(position.x)
    val y = view.translateY(position.y)
    // Todo: fix
    val width = view.translateWidth(player.getDimensions.width)
    val height = view.translateHeight(player.getDimensions.height)
    batch.draw(playerTexture, x, y, width, height)
  }

  def dispose(): Unit = {
    batch.dispose()
  }
}
