package mwittmann.spooktober.screen

package me.mwittmann.hellogdx.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.{DebugDraw, DebugLog, GlobalRandom}

class GameScreen() extends ScreenAdapter {

  Assets.load()

  var gameState = GameState()
  val gameObjects = new GameObjects(gameState.gameDimensions)
  val gameObjectsRenderer = new GameObjectsRenderer
  val debug = new DebugDraw

  override def render(delta: Float): Unit = {
    gameState = GameInput.movePlayer(delta, gameObjects, gameState)

    gameObjects.moveZombies(delta)
    super.render(delta)
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    val screenWidth = Gdx.graphics.getWidth
    val screenHeight = Gdx.graphics.getHeight
    val gameWidth = screenWidth * gameState.gameFactor
    val gameHeight = screenHeight * gameState.gameFactor
    // Todo: Probably don't offset this (see View)
    val view = new View(
      gameObjects.getPlayerPosition.x + gameObjects.getPlayerDimensions.width / 2,
      gameObjects.getPlayerPosition.y + gameObjects.getPlayerDimensions.height / 2,
      gameWidth,
      gameHeight,
      screenWidth / 2,
      screenHeight / 2,
      screenWidth,
      screenHeight
    )
    gameObjectsRenderer.render(gameObjects, view)
    debug.tick(delta, gameObjects)
  }

  override def dispose(): Unit = {
    gameObjectsRenderer.dispose()
    Assets.dispose()
  }
}
