package mwittmann.spooktober

import com.badlogic.gdx.{Game => GdxGame}
import com.badlogic.gdx.Gdx
import mwittmann.spooktober.screen.me.mwittmann.hellogdx.screen.GameScreen


class Game extends GdxGame {
    override def create(): Unit = {
        Gdx.graphics.setTitle("Zombies !!!")
        this.setScreen(new GameScreen)
    }

    override def render(): Unit = {
        super.render()
    }
}
