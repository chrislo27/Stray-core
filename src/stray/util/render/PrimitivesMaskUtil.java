package stray.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class PrimitivesMaskUtil {

	/**
	 * call this BEFORE rendering with ShapeRenderer and BEFORE drawing sprites
	 */
	public static void prepareMask() {
		Gdx.gl.glDepthFunc(GL20.GL_LESS);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glDepthMask(true);
		Gdx.gl.glColorMask(false, false, false, false);
	}

	/**
	 * call this AFTER batch.begin() and BEFORE drawing sprites
	 */
	public static void resetMask() {
		Gdx.gl.glColorMask(true, true, true, true);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
	}
}
