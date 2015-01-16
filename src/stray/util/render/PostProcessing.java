package stray.util.render;

import stray.Main;
import stray.util.MathHelper;

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
	 * 
	 * @param batch
	 * @param buffer
	 * @param blurshader
	 * @param radius
	 *            amount in pixels to blur
	 */
	public static void twoPassBlur(Batch batch, FrameBuffer buffer, ShaderProgram blurshader,
			float radius) {
		buffer.begin();
		batch.setShader(blurshader);
		blurshader.setUniformf("radius", (float) radius);
		blurshader.setUniformf("dir", 1f, 0f);
		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false, false);
		batch.flush();
		batch.setShader(null);
		buffer.end();

		buffer.begin();
		batch.setShader(blurshader);
		blurshader.setUniformf("dir", 0f, 1f);
		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false, false);
		batch.setShader(null);
		batch.flush();
		buffer.end();

		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), buffer.getWidth(),
				-buffer.getHeight());
		batch.flush();
	}

	/**
	 * makes the screen rainbowy and move 
	 * @param batch
	 * @param buffer
	 * @param saturation default 0.4f
	 * @param displace default 6
	 */
	public static void euphoria(Batch batch, FrameBuffer buffer, float saturation, float displace) {
		batch.setColor(Main.getRainbow(0.5f, saturation).r, Main.getRainbow(0.5f, saturation).g,
				Main.getRainbow(0.5f, saturation).b, saturation);
		batch.draw(buffer.getColorBufferTexture(), (displace * 2 * MathHelper.clampHalf(1f)),
				Gdx.graphics.getHeight(), buffer.getWidth(), -buffer.getHeight());
		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight()
				+ (displace * 2 * MathHelper.clampHalf(1f)), buffer.getWidth(), -buffer.getHeight());
		batch.draw(buffer.getColorBufferTexture(), (-displace * 2 * MathHelper.clampHalf(1f)),
				Gdx.graphics.getHeight(), buffer.getWidth(), -buffer.getHeight());
		batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight()
				- (displace * 2 * MathHelper.clampHalf(1f)), buffer.getWidth(), -buffer.getHeight());
		batch.flush();
		batch.setColor(1, 1, 1, 1);
	}
	
	/**
	 * synthetic method that has defaults
	 * @param batch
	 * @param buffer
	 */
	public static void euphoria(Batch batch, FrameBuffer buffer){
		euphoria(batch, buffer, 0.4f, 6);
	}
}
