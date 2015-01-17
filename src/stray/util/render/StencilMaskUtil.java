package stray.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class StencilMaskUtil {

	/**
	 * call this BEFORE rendering with ShapeRenderer and BEFORE drawing sprites, and AFTER what you want in the background rendered
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
	public static void useMask() {
		Gdx.gl.glDepthMask(false);
		Gdx.gl.glColorMask(true, true, true, true);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
	}
	
	/**
	 * call this AFTER batch.flush/end
	 */
	public static void resetMask(){
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	}
}
