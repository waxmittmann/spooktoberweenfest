package mwittmann.spooktober.asset

package me.mwittmann.hellogdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

object Assets {
  var zombieSheet: Texture = null
  var playerSheet: Texture  = null

  var player: TextureRegion = null
  var zombieA: Animation = null
  var zombieB: Animation = null

  def load(): Unit = {
    zombieSheet = loadTexture("ZombiesSpritesheet.png")

    playerSheet = loadTexture("PlayerSpritesheet.png")

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
