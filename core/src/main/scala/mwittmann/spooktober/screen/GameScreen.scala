package mwittmann.spooktober.screen

package me.mwittmann.hellogdx.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.pipeline.{Pipeline, State}
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.{DebugDraw, DebugLog, GlobalRandom}

class GameScreen() extends ScreenAdapter {

  Assets.load()

  //var gameState = GameState(view = View.emptyView)


  var gameState = State(Dimensions2df(800, 800))
  val pipeline = Pipeline.standardPipeline

  //val gameObjects = new GameObjects(gameState.gameDimensions)
  //val gameObjectsRenderer = new GameObjectsRenderer

  override def render(delta: Float): Unit = {
    //super.render(delta)
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    gameState = pipeline.run(gameState.copy(
      view = calculateView(gameState),
      input = GameInput.getInput
    ))

    //gameState = GameInput.handle(delta, gameObjects, gameState)
 //  gameObjects.moveZombies(calculateView, delta)
    ///gameObjectsRenderer.render(gameObjects, calculateView)
  }

  private def calculateView(state: State) = {
    import state.imap

    val screenWidth = Gdx.graphics.getWidth
    val screenHeight = Gdx.graphics.getHeight
    val gameWidth = screenWidth * gameState.gameFactor
    val gameHeight = screenHeight * gameState.gameFactor
    // Todo: Probably don't offset this (see View)
    val view = new View(
      state.player.getPosition.x
      //gameObjects.getPlayerPosition.x
//        + gameObjects.getPlayerDimensions.width / 2
        + state.player.getDimensions.width / 2
        - gameWidth / 2,
      state.player.getPosition.y
//      gameObjects.getPlayerPosition.y
//        + gameObjects.getPlayerDimensions.height / 2
        + state.player.getDimensions.height / 2
        - gameHeight / 2,
      gameWidth,
      gameHeight,
      0,
      0,
      screenWidth,
      screenHeight
    )
    view
  }

  override def dispose(): Unit = {
    pipeline.cleanup
    Assets.dispose()
  }
}
