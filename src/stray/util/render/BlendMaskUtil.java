package stray.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class BlendMaskUtil {

	public static void drawAlphaMask(Batch batch, Texture alphamask, float x, float y, float width,
			float height) {
		// disable RGB color, only enable ALPHA to the frame buffer
		Gdx.gl.glColorMask(false, false, false, true);

		// change the blending function for our alpha map
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);

		// draw alpha mask sprite(s)
		batch.draw(alphamask, x, y, width, height);

		// flush the batch to the GPU
		batch.flush();
	}

	public static void drawAlphaMask(Batch batch, Texture alphamask, float x, float y) {
		drawAlphaMask(batch, alphamask, x, y, alphamask.getWidth(), alphamask.getHeight());
	}

	public static void drawSpriteToMask(Batch batch, Texture sprite, int x, int y, int width, int height) {
		// now that the buffer has our alpha, we simply draw the sprite with the
		// mask applied
		Gdx.gl.glColorMask(true, true, true, true);
		batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

		// The scissor test is optional, but it depends
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glScissor(x, y, width, height);

		// draw our sprite to be masked
		batch.draw(sprite, 0, 0, 250, 250);

		// remember to flush before changing GL states again
		batch.flush();

		// disable scissor before continuing
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
	}
	
	public static void drawSpriteToMask(Batch batch, Texture sprite, int x, int y){
		drawSpriteToMask(batch, sprite, x, y, sprite.getWidth(), sprite.getHeight());
	}

}
