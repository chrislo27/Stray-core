package stray;

import stray.ui.Container;

import com.badlogic.gdx.Screen;

public abstract class Updateable implements Screen {

	public Main main;
	public Container container = new Container();

	public Updateable(Main m) {
		main = m;
	}

	@Override
	public abstract void render(float delta);
	
	/**
	 * updates once a render call only if this screen is active
	 */
	public abstract void renderUpdate();

	public abstract void tickUpdate();

	/**
	 * x is 5
	 */
	public abstract void renderDebug(int starting);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void show();

	@Override
	public abstract void hide();

	@Override
	public abstract void pause();

	@Override
	public abstract void resume();

	@Override
	public abstract void dispose();

	public int getDebugOffset() {
		return 0;
	}
	

}
