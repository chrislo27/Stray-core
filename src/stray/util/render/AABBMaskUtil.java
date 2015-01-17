package stray.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class AABBMaskUtil {

	/**
	 * call BEFORE batch.begin()
	 * @param clipX
	 * @param clipY
	 * @param clipWidth
	 * @param clipHeight
	 */
	public static void beginClipping(int clipX, int clipY, int clipWidth, int clipHeight){
		Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl20.glScissor(clipX, clipY, clipWidth, clipHeight);
	}
	
	/**
	 * call AFTER batch.end() or batch.flush()
	 */
	public static void endClipping(){
		Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
	}
}
