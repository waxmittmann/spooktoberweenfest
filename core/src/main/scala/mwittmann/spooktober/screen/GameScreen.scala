package mwittmann.spooktober.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.pipeline.Pipeline
import mwittmann.spooktober.pipeline.state.{State, ViewState}
import mwittmann.spooktober.unit.Dimensions2df

class GameScreen() extends ScreenAdapter {

  Assets.load()

  var gameState = State(Dimensions2df(800, 800))
  val pipeline = Pipeline.standardPipeline

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    val view = calculateView(gameState)
    gameState = pipeline.run(gameState.copy(
      view = view,
      input = GameInput.getInput(view),
      delta = delta
    ))
  }

  private def calculateView(state: State) = {
    import state.imap

    val screenWidth = Gdx.graphics.getWidth
    val screenHeight = Gdx.graphics.getHeight
    val gameWidth = screenWidth * gameState.gameFactor
    val gameHeight = screenHeight * gameState.gameFactor
    val gameX =
      state.player.getPosition.x +
        state.player.getDimensions.width / 2 -
        gameWidth / 2
    val gameY = state.player.getPosition.y +
      state.player.getDimensions.height / 2 -
      gameHeight / 2

    val view = new ViewState(
      gameX,
      gameY,
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
