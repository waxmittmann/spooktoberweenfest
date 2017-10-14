package mwittmann.spooktober.pipeline.state

import com.badlogic.gdx.graphics.g2d.TextureRegion
import mwittmann.spooktober.util.MathUtils

case class TerrainState(
  terrainTextures: Map[Int, TextureRegion],
  tileSize: Float,
  tiles: MathUtils.Array2d[Int]
) {
  assert(tiles.nonEmpty && tiles(0).nonEmpty)

  def mapWidth = tiles.length * tileSize

  def mapHeight = tiles(0).length * tileSize
}
