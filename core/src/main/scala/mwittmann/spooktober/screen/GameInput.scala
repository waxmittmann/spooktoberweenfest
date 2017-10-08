package mwittmann.spooktober.screen

import com.badlogic.gdx.{Gdx, Input => GdxInput}
import mwittmann.spooktober.pipeline.Input
import mwittmann.spooktober.unit.Direction._
import mwittmann.spooktober.unit.Direction

object GameInput {

  def getInput: Input = {
    val isW = Gdx.input.isKeyPressed(GdxInput.Keys.W)
    val isA = Gdx.input.isKeyPressed(GdxInput.Keys.A)
    val isD = Gdx.input.isKeyPressed(GdxInput.Keys.D)
    val isS = Gdx.input.isKeyPressed(GdxInput.Keys.S)

    val moveHoriz = if (isA && !isD) Direction.Left else if (!isA && isD) Direction.Right else Neutral
    val moveVert = if (isW && !isS) Up else if (!isW && isS) Down else Neutral

    println(moveHoriz + ", " + moveVert)

    val movement = (moveHoriz, moveVert) match {
      case (Direction.Left, Down) => LowerLeft
      case (Direction.Left, Neutral) => Direction.Left
      case (Direction.Left, Up) => UpperLeft

      case (Neutral, Down) => Down
      case (Neutral, Neutral) => Neutral
      case (Neutral, Up) => Up

      case (Direction.Right, Down) => LowerRight
      case (Direction.Right, Neutral) => Direction.Right
      case (Direction.Right, Up) => UpperRight
    }

    println(movement)

    Input(movement, Gdx.input.isKeyPressed(GdxInput.Keys.ENTER))
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
