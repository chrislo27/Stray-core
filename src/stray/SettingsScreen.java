package stray;

import stray.ui.LanguageButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class SettingsScreen extends Updateable{

	public SettingsScreen(Main m) {
		super(m);
		
		container.elements.add(new LanguageButton(5, 5));
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
