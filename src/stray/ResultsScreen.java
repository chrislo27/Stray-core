package stray;

import stray.ui.LevelSelectButton;
import stray.ui.NextLevelButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class ResultsScreen extends Updateable {

	public ResultsScreen(Main m) {
		super(m);
		
		container.elements.add(new LevelSelectButton(5, 5){

			@Override
			public boolean onLeftClick() {
				return true;
			}
			
		});
		
		container.elements.add(new NextLevelButton(58, 5){

			@Override
			public boolean onLeftClick() {
				return false;
			}
			
		});
	}

	private String levelfile = null;

	public ResultsScreen setData(String levelf) {

		return this;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (levelfile != null) {
			main.font.setColor(1, 1, 1, 1);
			main.drawCentered(
					Translator.getMsg("menu.results.latesttime")
							+ main.progress.getLong(levelfile + "-latesttime"),
					Gdx.graphics.getWidth() / 2, Main.convertY(250));
			main.drawCentered(
					Translator.getMsg("menu.results.besttime")
							+ main.progress.getLong(levelfile + "-latesttime"),
					Gdx.graphics.getWidth() / 2, Main.convertY(275));
		}
	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
