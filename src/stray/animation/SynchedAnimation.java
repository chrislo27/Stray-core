package stray.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class SynchedAnimation implements Disposable {

	public float framedelay = 0.05f;
	public int framecount = 1;
	protected TextureRegion[] frames;
	String path;
	private int tilewidth, tileheight;
	public boolean usesRegion = false;
	boolean vertical = true;

	public SynchedAnimation(float delay, int count, String path, boolean usesRegion) {
		framedelay = delay;
		framecount = count;
		this.path = path;
		this.usesRegion = usesRegion;
		if (count > 0) frames = new TextureRegion[count];
	}

	public SynchedAnimation setRegionTile(int width, int height) {
		tilewidth = width;
		tileheight = height;
		return this;
	}

	public SynchedAnimation setVertical(boolean v) {
		vertical = v;
		return this;
	}

	public void load() {
		if (usesRegion) {
			Texture tex = new Texture(path);
			if (vertical) {
				for (int y = 0; y < (tex.getHeight() / tileheight); y++) {
					frames[y] = new TextureRegion(tex, 0, y * (tileheight), tilewidth, tileheight);
				}
			} else {
				for (int x = 0; x < (tex.getWidth() / tilewidth); x++) {
					frames[x] = new TextureRegion(tex, x * tilewidth, 0, tilewidth, tileheight);
				}
			}
		} else {
			String suffix = path.substring(path.lastIndexOf('.'));
			String withoutSuffix = path.substring(0, path.lastIndexOf('.'));
			for (int i = 0; i < frames.length; i++) {
				Texture t = new Texture(withoutSuffix + i + suffix);
				frames[i] = new TextureRegion(t);
			}
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
