package mwittmann.spooktober.screen

import com.badlogic.gdx.{Gdx, Input}
import mwittmann.spooktober.entity.Zombie
import mwittmann.spooktober.unit.{Position2d, Vector2df}
import mwittmann.spooktober.util.GlobalRandom

object GameInput {
  def movePlayer(
    delta: Float,
    gameObjects: GameObjects,
    gameState: GameState
  ): GameState = {
    import gameState._

    var newGameFactor = gameFactor

    if (Gdx.input.isKeyPressed(Input.Keys.W)) gameObjects.movePlayer(Vector2df(0f, playerSpeed * delta * factor))

    if (Gdx.input.isKeyPressed(Input.Keys.A)) gameObjects.movePlayer(Vector2df(-playerSpeed * delta * factor, 0f))

    if (Gdx.input.isKeyPressed(Input.Keys.D)) gameObjects.movePlayer(Vector2df(playerSpeed * delta * factor, 0f))

    if (Gdx.input.isKeyPressed(Input.Keys.S)) gameObjects.movePlayer(Vector2df(0f, -playerSpeed * delta * factor))

    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
      for { _ <- 0 to 200 } yield {
        val x = GlobalRandom.random.nextInt(gameDimensions.x.toInt)
        val y = GlobalRandom.random.nextInt(gameDimensions.y.toInt)
        gameObjects.addZombie(Zombie(Position2d(x, y)))
      }
    }

    if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) newGameFactor *= (1 + 0.5f * delta)

    if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) newGameFactor /= (1 + 0.5f * delta)

    if (gameFactor < 0.01) newGameFactor = 0.01f

    if (gameFactor > 10.0f) newGameFactor = 10.0f

    gameState.copy(gameFactor = newGameFactor)
  }
}
