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

  var gameState = GameState(view = View.emptyView)
  val gameObjects = new GameObjects(gameState.gameDimensions)
  val gameObjectsRenderer = new GameObjectsRenderer

  override def render(delta: Float): Unit = {
    //super.render(delta)
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    gameState = gameState.copy(view = calculateView)
    gameState = GameInput.handle(delta, gameObjects, gameState)
    gameObjects.moveZombies(calculateView, delta)

    gameObjectsRenderer.render(gameObjects, calculateView)
  }

  private def calculateView = {
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
    view
  }

  override def dispose(): Unit = {
    gameObjectsRenderer.dispose()
    Assets.dispose()
  }
}
