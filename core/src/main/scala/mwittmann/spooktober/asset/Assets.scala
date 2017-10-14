package mwittmann.spooktober.asset

package me.mwittmann.hellogdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin

// Todo: Rewrite immutable
object Assets {
  var zombieSheet: Texture = null
  var playerSheet: Texture  = null
  var skin: Skin = null

  var player: TextureRegion = null
  var zombieA: Animation = null
  var zombieB: Animation = null

  var terrainSheet: Texture = null
  var terrainTextures: List[TextureRegion] = null

  def loadUISkin(): Skin = new Skin(Gdx.files.internal("skin2/comic-ui.json"))

  def loadTerrain(): Unit = {
    terrainSheet = loadTexture("TerrainMap.png")

    if ((terrainSheet.getWidth % 200) != 0)
      throw new Exception("Terrain tiles must be 200 wide")

    if ((terrainSheet.getHeight % 200) != 0)
      throw new Exception("Terrain tiles must be 200 high")


//    terrainTextures = List(
//      new TextureRegion(terrainSheet, 0, 0, 400, 400)
//    )

    terrainTextures = (for {
        x <- 0 until terrainSheet.getWidth / 200
        y <- 0 until terrainSheet.getHeight / 200
      } yield {
        new TextureRegion(terrainSheet, x * 200, y * 200, 200, 200)
      }).toList
  }

  def load(): Unit = {
    loadTerrain()

    zombieSheet = loadTexture("ZombiesSpritesheet.png")

    playerSheet = loadTexture("PlayerSpritesheet.png")

    skin = loadUISkin()

    zombieA = new Animation(
      0.2f,
      Seq(
        new TextureRegion(zombieSheet, 0, 0, 200, 200),
        new TextureRegion(zombieSheet, 0, 200, 200, 200),
        new TextureRegion(zombieSheet, 0, 400, 200, 200),
        new TextureRegion(zombieSheet, 0, 600, 200, 200)
      )
    )

    zombieB = new Animation(
      0.2f,
      Seq(
        new TextureRegion(zombieSheet, 200, 0, 200, 200),
        new TextureRegion(zombieSheet, 200, 200, 200, 200),
        new TextureRegion(zombieSheet, 200, 400, 200, 200),
        new TextureRegion(zombieSheet, 200, 600, 200, 200)
      )
    )

    player = new TextureRegion(playerSheet, 0, 0, 200, 200)
  }

  def loadTexture(file: String): Texture = new Texture(Gdx.files.internal(file))

  def dispose(): Unit = zombieSheet.dispose()
}
