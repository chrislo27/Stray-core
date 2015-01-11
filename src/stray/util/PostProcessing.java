package stray.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * 
 * helper class for applying post-processing and rendering it
 *
 */
public class PostProcessing {

	/**
	 * gaussian blur on the entire screen
	 * @param batch
	 * @param buffer
	 * @param blurshader
	 * @param radius amount in pixels to blur
	 */
	public static void twoPassBlur(Batch batch, FrameBuffer buffer, ShaderProgram blurshader, float radius){
		buffer.begin();
		batch.setShader(blurshader);
		blurshader.setUniformf("radius", (float) radius);
		blurshader.setUniformf("dir", 1f, 0f);
		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
		batch.setShader(null);
		batch.flush();
		buffer.end();

		buffer.begin();
		blurshader.setUniformf("dir", 0f, 1f);
		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
		batch.setShader(null);
		batch.flush();
		buffer.end();

		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				buffer.getWidth(), -buffer.getHeight());
		batch.flush();
	}
}
