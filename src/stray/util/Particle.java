package stray.util;

import stray.Main;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Particle implements Poolable {

	float x = 0;
	float y = 0;
	float velox = 0;
	float veloy = 0;
	public float lifetime = -1;
	float prelife = 0;
	boolean destroyOnBlock = false;
	
	boolean clockwise = true;
	float rotspeed = 0;

	float tintr = 1;
	float tintg = 1;
	float tintb = 1;
	float tinta = 1;

	String texture = "poof";

	public Particle() {

	}

	public Particle setVelocity(float x, float y) {
		velox = x;
		veloy = y;
		return this;
	}

	public Particle setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Particle setTexture(String t) {
		texture = t;
		return this;
	}

	public Particle setLifetime(float sec) {
		lifetime = sec;
		return this;
	}

	public Particle setPrelife(float sec) {
		prelife = sec;
		return this;
	}

	public Particle setTint(Color c) {
		tintr = c.r;
		tintg = c.g;
		tintb = c.b;
		tinta = c.a;
		return this;
	}

	public Particle setTint(float r, float g, float b, float a) {
		tintr = r;
		tintg = g;
		tintb = b;
		tinta = a;
		return this;
	}

	public Particle setAlpha(float a) {
		tinta = a;
		return this;
	}

	public Particle setDestroyOnBlock(boolean b) {
		destroyOnBlock = b;
		return this;
	}
	
	public Particle setRotation(float seconds, boolean clockwise){
		rotspeed = seconds;
		this.clockwise = clockwise;
		return this;
	}

	@Override
	public void reset() {
		x = 0;
		y = 0;
		velox = 0;
		veloy = 0;
		lifetime = -1;
		prelife = 0;
		texture = "poof";
		destroyOnBlock = false;
		clockwise = true;
		rotspeed = 0;
		setTint(Color.WHITE);
	}

	public void render(World world, Main main) {
		if (prelife <= 0) {
			update(Gdx.graphics.getDeltaTime());

			if (texture != null) {
				if (texture.startsWith("_")) {
					main.font.setColor(tintr, tintg, tintb,
							(lifetime <= 0.1f ? (Math.min(lifetime * 10f, tinta)) : tinta));
					main.drawCentered(texture.substring(1), x * World.tilesizex
							- world.camera.camerax,
							Main.convertY(y * World.tilesizey - world.camera.cameray));
					main.font.setColor(Color.WHITE);
				} else if (texture.startsWith("real-")) {
					Texture t = main.manager.get(texture.substring(5), Texture.class);
					main.batch.setColor(tintr, tintg, tintb,
							(lifetime <= 0.1f ? (Math.min(lifetime * 10f, tinta)) : tinta));
					if(rotspeed > 0){
						Utils.drawRotated(main.batch, t, x * World.tilesizex - (t.getWidth() / 2) - world.camera.camerax,
							Main.convertY(y * World.tilesizey + (t.getHeight() / 2)
									- world.camera.cameray), t.getWidth(), t.getHeight(), MathHelper.getNumberFromTime(rotspeed) * 360, clockwise);
					}else{
						main.batch.draw(
								t,
								x * World.tilesizex - (t.getWidth() / 2) - world.camera.camerax,
								Main.convertY(y * World.tilesizey + (t.getHeight() / 2)
										- world.camera.cameray));
					}
					main.batch.setColor(Color.WHITE);
				} else {
					Texture t = main.manager.get(AssetMap.get(texture), Texture.class);
					main.batch.setColor(tintr, tintg, tintb,
							(lifetime <= 0.1f ? (Math.min(lifetime * 10f, tinta)) : tinta));
					main.batch.draw(
							t,
							x * World.tilesizex - (t.getWidth() / 2) - world.camera.camerax,
							Main.convertY(y * World.tilesizey + (t.getHeight() / 2)
									- world.camera.cameray));
					if(rotspeed > 0){
						Utils.drawRotated(main.batch, t, x * World.tilesizex - (t.getWidth() / 2) - world.camera.camerax,
								Main.convertY(y * World.tilesizey + (t.getHeight() / 2)
										- world.camera.cameray), t.getWidth(), t.getHeight(), MathHelper.getNumberFromTime(rotspeed) * 360, clockwise);
					}else{
						main.batch.draw(
								t,
								x * World.tilesizex - (t.getWidth() / 2) - world.camera.camerax,
								Main.convertY(y * World.tilesizey + (t.getHeight() / 2)
										- world.camera.cameray));
					}
					main.batch.setColor(Color.WHITE);
				}
			}

		}

		if (prelife > 0) {
			prelife -= Gdx.graphics.getDeltaTime();
		} else if (prelife <= 0) {
			lifetime -= Gdx.graphics.getDeltaTime();
		}

		if (destroyOnBlock) {
			if (world.getBlock((int) x, (int) y).isSolid(world, (int) x, (int) y)) if (MathHelper
					.intersects((int) x, (int) y, 1, 1, x - (4 * World.tilepartx), y - (4 * World.tileparty), 8 * World.tilepartx,
							8 * World.tileparty)) {
				lifetime = -1;
				prelife = -1;
			}
		}

	}

	private void update(float delta) {
		x += velox * delta;
		y += veloy * delta;
	}

}
