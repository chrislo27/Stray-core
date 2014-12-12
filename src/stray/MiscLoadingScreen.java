package stray;

import stray.util.MathHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class MiscLoadingScreen extends Updateable {

	public MiscLoadingScreen(Main m) {
		super(m);
	}


	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
//		main.font.setColor(Color.GREEN);
//		main.font.draw(main.batch, "LOADING"
//				+ getLoadingDots()
//				+ (percentLoaded > -1 ? " - "
//						+ (String.format("%.1f", (percentLoaded * 100f)) + "%") : ""), 5, 20);
		Main.gears.render(main, Gdx.graphics.getWidth() - (128 + 64), 0);
		main.batch.end();
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

	@Override
	public void renderUpdate() {
	}

}
