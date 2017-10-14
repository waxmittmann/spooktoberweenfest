package mwittmann.spooktober.pipeline.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import mwittmann.spooktober.asset.me.mwittmann.hellogdx.asset.Assets
import mwittmann.spooktober.pipeline.PipelineStage
import mwittmann.spooktober.pipeline.state.State

class UIRenderStage extends PipelineStage {
  // Todo: Reuse batch
  private val stage: Stage = new Stage()
  Gdx.input.setInputProcessor(stage)


  val table = new Table()
  table.setFillParent(true)
  table.setSkin(Assets.skin)
  table.setPosition(200, 200)
  table.add("Hello World")
  stage.addActor(table)
  table.setDebug(true) // This is optional, but enables debug lines for tables.

  override def run(state: State) = {
    stage.act(state.delta)
    stage.draw()

    state
  }

  /*
  private Stage stage;
private Table table;

public void create () {
	stage = new Stage();
	Gdx.input.setInputProcessor(stage);

	table = new Table();
	table.setFillParent(true);
	stage.addActor(table);

	table.setDebug(true); // This is optional, but enables debug lines for tables.

	// Add widgets to the table here.
}

public void resize (int width, int height) {
	stage.getViewport().update(width, height, true);
}

public void render () {
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	stage.act(Gdx.graphics.getDeltaTime());
	stage.draw();
}

public void dispose() {
	stage.dispose();
}
   */

}
