package stray.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class StencilMaskUtil {

	/**
	 * call this BEFORE rendering with ShapeRenderer and BEFORE drawing sprites, and AFTER what you want in the background rendered
	 */
	public static void prepareMask() {
		Gdx.gl20.glDepthFunc(GL20.GL_LESS);

		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl20.glDepthMask(true);
		Gdx.gl20.glColorMask(false, false, false, false);
	}

	/**
	 * call this AFTER batch.begin() and BEFORE drawing sprites
	 */
	public static void useMask() {
		Gdx.gl20.glDepthMask(false);
		Gdx.gl20.glColorMask(true, true, true, true);

		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl20.glDepthFunc(GL20.GL_EQUAL);
	}
	
	/**
	 * call this AFTER batch.flush/end
	 */
	public static void resetMask(){
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
	}
}
