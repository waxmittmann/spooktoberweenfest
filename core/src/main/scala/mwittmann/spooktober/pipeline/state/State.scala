package mwittmann.spooktober.pipeline.state

import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.entity.Entity
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.Map2d

case class State(
  zombies: ZombieState = new ZombieState(),
  player: PlayerState,
  map: Map2d[Entity],
  view: ViewState = ViewState.emptyView,
  input: InputState = InputState.NoInput,
  delta: Float = 0.0f,
  gameFactor: Float = 0.1f,
  factor: Float = 5.0f,
  playerSpeed: Float = 10,
  gameDimensions: Dimensions2df,
  terrainState: TerrainState
) {
  assert(terrainState.mapWidth == gameDimensions.width && terrainState.mapHeight == gameDimensions.height)

  implicit val imap = map
}

object State {
  def apply(gameDimensions: Dimensions2df): State = {
    val map = new Map2d[Entity](gameDimensions, Dimensions2df(20, 20))
    val playerState = PlayerState(map, Position2df(50, 50))

    State(
      player = playerState,
      gameDimensions = gameDimensions,
      map = map,
      terrainState = initTerrainState(gameDimensions)
    )
  }

  def initTerrainState(gameDimensions: Dimensions2df): TerrainState = {
//    val textures = Map(
//      0 -> Assets.zombieA.keyFrames.head,
//      1 -> Assets.zombieB.keyFrames.head,
//    )

    val textures = Assets.terrainTextures.zipWithIndex.map(_.swap).toMap

    val tileDims = 10

    val tiles = Array.ofDim[Int]((gameDimensions.width / tileDims).toInt, (gameDimensions.height / tileDims).toInt)

    for {
      xAt <- 0 until (gameDimensions.width / tileDims).toInt
      yAt <- 0 until (gameDimensions.height / tileDims).toInt
    } yield {
      tiles(xAt)(yAt) = (xAt + yAt) % textures.size
    }

    TerrainState(textures, 10, tiles)

  }
}