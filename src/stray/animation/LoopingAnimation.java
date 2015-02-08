package stray.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class LoopingAnimation extends Animation{

	public LoopingAnimation(float delay, int count, String path, boolean usesRegion) {
		super(delay, count, path, usesRegion);
	}

	@Override
	public TextureRegion getCurrentFrame() {
		return getCurrentFrame(framedelay);
	}

	public TextureRegion getCurrentFrame(float delay) {
		long i = (long) (System.currentTimeMillis() / (delay * 1000d));
		return frames[(int) ((frames.length - 1) - (i % frames.length))];
	}

}
