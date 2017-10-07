package mwittmann.spooktober.screen

import com.badlogic.gdx.{Gdx, Input => GdxInput}
import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.unit.{Dimensions2df, Position2df, Vector2df}
import mwittmann.spooktober.util.{GlobalRandom, MapStorable}
import mwittmann.spooktober.pipeline
import mwittmann.spooktober.pipeline.{Down, Input, LowerLeft, LowerRight, Neutral, Up, UpperLeft, UpperRight}

object GameInput {

  def getInput: Input = {
    val isW = Gdx.input.isKeyPressed(GdxInput.Keys.W)
    val isA = Gdx.input.isKeyPressed(GdxInput.Keys.W)
    val isD = Gdx.input.isKeyPressed(GdxInput.Keys.W)
    val isS = Gdx.input.isKeyPressed(GdxInput.Keys.W)


    val moveHoriz = if (isA && !isD) pipeline.Left else if (!isA && isD) pipeline.Right else pipeline.Neutral
    val moveVert = if (isW && !isS) pipeline.Up else if (!isW && isS) pipeline.Down else pipeline.Neutral

    val movement = (moveHoriz, moveVert) match {
      case (Left, Down) => LowerLeft
      case (Left, Neutral) => pipeline.Left
      case (Left, Up) => UpperLeft

      case (Neutral, Down) => Down
      case (Neutral, Neutral) => Neutral
      case (Neutral, Up) => Up

      case (Right, Down) => LowerRight
      case (Right, Neutral) => pipeline.Right
      case (Right, Up) => UpperRight
    }

    Input(movement)
  }

  /*

  // Mutates gameObjects, which maybe isn't so nice; perhaps we should return a mutating object here
  def handle(
    delta: Float,
    gameObjects: GameObjects,
    gameState: GameState
  ): GameState = {
    import gameState._

    var newGameFactor = gameFactor

    if (Gdx.input.isKeyPressed(Input.Keys.W)) gameObjects.movePlayer(gameState.view, Vector2df(0f, playerSpeed * delta * factor))

    if (Gdx.input.isKeyPressed(Input.Keys.A)) gameObjects.movePlayer(gameState.view, Vector2df(-playerSpeed * delta * factor, 0f))

    if (Gdx.input.isKeyPressed(Input.Keys.D)) gameObjects.movePlayer(gameState.view, Vector2df(playerSpeed * delta * factor, 0f))

    if (Gdx.input.isKeyPressed(Input.Keys.S)) gameObjects.movePlayer(gameState.view, Vector2df(0f, -playerSpeed * delta * factor))

    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
      for { _ <- 0 to 200 } yield {
        val x = GlobalRandom.random.nextInt(gameDimensions.width.toInt)
        val y = GlobalRandom.random.nextInt(gameDimensions.height.toInt)
        val zombie = new Zombie()
        gameObjects.addZombie(MapStorable(Position2df(x, y), zombie.getDimensions, zombie))
      }
    }

    if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) newGameFactor *= (1 + 0.5f * delta)

    if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) newGameFactor /= (1 + 0.5f * delta)

    if (gameFactor < 0.01) newGameFactor = 0.01f

    if (gameFactor > 10.0f) newGameFactor = 10.0f

    gameState.copy(gameFactor = newGameFactor)
  }
  */
}
