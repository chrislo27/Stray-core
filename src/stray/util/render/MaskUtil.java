package stray.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MaskUtil {

	/**
	 * Start defining the screen mask. After calling this use graphics functions
	 * to mask out the area
	 */
	public static void defineMask() {
		Gdx.gl20.glDepthMask(true);
		Gdx.gl20.glClearDepthf(1);
		Gdx.gl20.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glDepthFunc(GL20.GL_ALWAYS);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDepthMask(true);
		Gdx.gl20.glColorMask(false, false, false, false);
	}

	/**
	 * Finish defining the screen mask
	 */
	public static void finishDefineMask() {
		Gdx.gl20.glDepthMask(false);
		Gdx.gl20.glColorMask(true, true, true, true);
	}

	/**
	 * Start drawing only on the masked area
	 */
	public static void drawOnMask() {
		Gdx.gl20.glDepthFunc(GL20.GL_EQUAL);
	}

	/**
	 * Start drawing only off the masked area
	 */
	public static void drawOffMask() {
		Gdx.gl20.glDepthFunc(GL20.GL_NOTEQUAL);
	}

	/**
	 * Reset the masked area - should be done after you've finished rendering
	 */
	public static void resetMask() {
		Gdx.gl20.glDepthMask(true);
		Gdx.gl20.glClearDepthf(0);
		Gdx.gl20.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glDepthMask(false);

		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
	}
}
