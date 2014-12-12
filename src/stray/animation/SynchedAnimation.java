package stray.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class SynchedAnimation implements Disposable {

	public float framedelay = 0.05f;
	public int framecount = 1;
	protected TextureRegion[] frames;
	String path;
	String suffix;
	private int tilewidth, tileheight;
	public boolean usesRegion = false;

	public SynchedAnimation(float delay, int count, String path, String suffix, boolean usesRegion) {
		framedelay = delay;
		framecount = count;
		this.path = path;
		this.suffix = suffix;
		this.usesRegion = usesRegion;
		if (count > 0) frames = new TextureRegion[count];
	}

	public SynchedAnimation setRegionTile(int width, int height) {
		tilewidth = width;
		tileheight = height;
		return this;
	}

	public void load() {
		if (usesRegion) {
			Texture tex = new Texture(path + suffix);
			for (int y = 0; y < (tex.getHeight() / tileheight); y++) {
				frames[y] = new TextureRegion(tex, 0, y * (tileheight), tilewidth, tileheight);
			}
		} else for (int i = 0; i < frames.length; i++) {
			Texture t = new Texture(path + i + suffix);
			frames[i] = new TextureRegion(t);
		}
	}

	public TextureRegion getCurrentFrame() {
		return getCurrentFrame(framedelay);
	}
	
	public TextureRegion getCurrentFrame(float delay) {
		long i = (long) (System.currentTimeMillis() / (delay * 1000d));
		return frames[(int) ((frames.length - 1) - (i % frames.length))];
	}

	@Override
	public void dispose() {
		if (usesRegion) {
			frames[0].getTexture().dispose();
			return;
		}
		for (int i = frames.length - 1; i > -1; i--) {
			frames[i].getTexture().dispose();
		}
	}

}
