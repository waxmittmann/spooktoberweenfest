package mwittmann.spooktober.screen

package me.mwittmann.hellogdx.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.unit.{Dimensions2d, Position, Vector2df}
import mwittmann.spooktober.util.{DebugDraw, GlobalRandom}

class GameScreen() extends ScreenAdapter {

  Assets.load()

  val gameDimensions = new Dimensions2d(800, 800)
  val gameObjects = new GameObjects(gameDimensions)
  val gameObjectsRenderer = new GameObjectsRenderer
  val debug = new DebugDraw
  val factor = 5.0f
  val playerSpeed: Float = 10

  private[screen] var sinceLast: Float = 0
  private[screen] val waitFor: Float = 0.025f
  private[screen] var gameFactor: Float = 0.1f

  override def render(deltaSeconds: Float): Unit = {
    sinceLast += deltaSeconds

    if (sinceLast >= waitFor) {
      reallyRender(sinceLast)
      sinceLast = 0
    }
  }

  def reallyRender(delta: Float): Unit = {
    movePlayer(delta)
    gameObjects.moveZombies(delta)
    super.render(delta)
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    val screenWidth = Gdx.graphics.getWidth
    val screenHeight = Gdx.graphics.getHeight
    val gameWidth = screenWidth * gameFactor
    val gameHeight = screenHeight * gameFactor
    val view = new View(gameObjects.player.getPosition.x + gameObjects.player.getDimensions.x / 2, gameObjects.player.getPosition.y + gameObjects.player.getDimensions.y / 2, gameWidth, gameHeight, screenWidth / 2, screenHeight / 2, screenWidth, screenHeight)
    gameObjectsRenderer.render(gameObjects, view)
    debug.tick(delta, gameObjects)
  }

  private def movePlayer(delta: Float): Unit = {
    if (Gdx.input.isKeyPressed(Input.Keys.W)) gameObjects.movePlayer(new Vector2df(0f, playerSpeed * delta * factor))
    if (Gdx.input.isKeyPressed(Input.Keys.A)) gameObjects.movePlayer(new Vector2df(-playerSpeed * delta * factor, 0f))
    if (Gdx.input.isKeyPressed(Input.Keys.D)) gameObjects.movePlayer(new Vector2df(playerSpeed * delta * factor, 0f))
    if (Gdx.input.isKeyPressed(Input.Keys.S)) gameObjects.movePlayer(new Vector2df(0f, -playerSpeed * delta * factor))
    if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) this.gameFactor *= (1 + 0.5f * delta)
    if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) this.gameFactor /= (1 + 0.5f * delta)
    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
      var i = 0
      while ( {
        i < 200
      }) {
        val x = GlobalRandom.random.nextInt(gameDimensions.x.toInt)
        val y = GlobalRandom.random.nextInt(gameDimensions.y.toInt)
        gameObjects.addZombie(new Zombie(new Position(x, y)))

        {
          i += 1; i - 1
        }
      }
    }
    if (this.gameFactor < 0.01) this.gameFactor = 0.01f
    if (this.gameFactor > 10.0f) this.gameFactor = 10.0f
  }

  override def dispose(): Unit = {
    gameObjectsRenderer.dispose()
    Assets.dispose()
  }
}
