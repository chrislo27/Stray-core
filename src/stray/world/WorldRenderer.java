package stray.world;

import stray.Main;
import stray.ParticlePool;
import stray.blocks.Blocks;
import stray.effect.Effect;
import stray.entity.Entity;
import stray.util.AssetMap;
import stray.util.Colors;
import stray.util.ElectricityRenderer;
import stray.util.MathHelper;
import stray.util.Message;
import stray.util.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class WorldRenderer {

	World world;
	SpriteBatch batch;
	Main main;

	Array<GearPart> gearparts = new Array<GearPart>();

	public WorldRenderer(World world) {
		this.world = world;
		batch = world.batch;
		main = world.main;
	}

	public boolean showGrid = false;

	public void renderBlocks() {
		main.font.setColor(Color.BLACK);
		float maxx = world.sizex;
		float maxy = world.sizey;
		int prex = (int) MathUtils.clamp(((world.camera.camerax / World.tilesizex) - 1), 0f, maxx);
		int prey = (int) MathUtils.clamp(((world.camera.cameray / World.tilesizey) - 1), 0f, maxy);
		int postx = (int) MathUtils.clamp((world.camera.camerax / World.tilesizex) + 2
				+ (Gdx.graphics.getWidth() / World.tilesizex), 0f, maxx);
		int posty = (int) MathUtils.clamp((world.camera.cameray / World.tilesizey) + 2
				+ (Gdx.graphics.getHeight() / World.tilesizex), 0f, maxy);

		main.font.setColor(Color.WHITE);
		for (int x = prex; x < postx; x++) {
			for (int y = prey; y < posty; y++) {
				if (world.getBlock(x, y) != null) {
					world.getBlock(x, y).render(world, x, y);
				} else {
					Blocks.defaultBlock().render(world, x, y);
				}
				if (showGrid) {
					main.batch.setColor(1, 1, 1, 0.5f);
					main.fillRect(
							x * world.tilesizex - world.camera.camerax,
							Main.convertY((y * world.tilesizey - world.camera.cameray)
									+ World.tilesizey), 1, World.tilesizey);
					main.fillRect(
							x * world.tilesizex - world.camera.camerax,
							Main.convertY((y * world.tilesizey - world.camera.cameray)
									+ World.tilesizey), World.tilesizex, 1);
					main.batch.setColor(1, 1, 1, 1);
				}
			}
		}

		batch.flush();

	}

	public void renderVoid() {
		batch.setColor(0, 0, 0, 1);
		main.fillRect(0, 0, ((world.getVoidDistance() * World.tilesizex) - world.camera.camerax),
				Gdx.graphics.getHeight());
		batch.draw(main.manager.get(AssetMap.get("voidend"), Texture.class),
				((world.getVoidDistance() * World.tilesizex) - world.camera.camerax), 0,
				main.manager.get(AssetMap.get("voidend"), Texture.class).getWidth(),
				Gdx.graphics.getHeight());
		batch.setColor(1, 1, 1, 1);
		for (int i = 0; i < 3; i++) {
			if (Main.random(1, 12) == 1) {
				float location = Main.random(1, Gdx.graphics.getHeight());
				ElectricityRenderer.drawP2PLightning(
						batch,
						((world.getVoidDistance() * World.tilesizex) - world.camera.camerax),
						location,
						((world.getVoidDistance() * World.tilesizex) - world.camera.camerax)
								+ Main.random(World.tilesizex * 1.25f, World.tilesizex * 2.25f),
						location + Main.random(-8, 8), 24, 1.5f, 3, 3,
						Colors.voidPurple.toFloatBits());
			}
		}
		int ylevel = Main.random(-World.tilesizex, Gdx.graphics.getHeight() + World.tilesizey);
		float velo = 0;
		if(ylevel > Gdx.graphics.getHeight() / 2f){
			velo = MathUtils.clamp(-(((ylevel - (Gdx.graphics.getHeight() / 2f)) / (Gdx.graphics.getHeight() / 2f)) * 4), -4f, 4f);
		}else{
			velo = MathUtils.clamp(4 - ((ylevel / (Gdx.graphics.getHeight() / 2f)) * 4), -4f, 4f);
		}
		
		world.particles.add(ParticlePool
				.obtain()
				.setTexture("magnetglow")
				.setTint(0, 0, 0, 1)
				.setLifetime(1.5f)
				.setVelocity(-2, velo)
				.setPosition((((world.getVoidDistance())) + 2 + Main.random(0.5f, 1.5f)),
						(world.camera.cameray + ylevel) / World.tilesizey));

	}

	public void renderEntities() {

		for (Entity e : world.entities) {
			e.render(Gdx.graphics.getDeltaTime());
			if (Main.debug) {
				batch.setColor(Color.MAGENTA.r, Color.MAGENTA.g, Color.MAGENTA.b, 0.1f);
				main.fillRect(
						(e.x * World.tilesizex) - world.camera.camerax,
						Main.convertY(((e.y * World.tilesizey) + (e.sizey * World.tilesizey))
								- world.camera.cameray), e.sizex * World.tilesizex, e.sizey
								* World.tilesizey);
				batch.setColor(Color.WHITE);
			}
		}
		batch.flush();
	}

	public void tickUpdate() {

	}

	public void renderUi() {
		if (world.getPlayer() == null) return;

		Color c;
		int offsety = 0;
		for (int i = world.msgs.size - 1; i >= 0; i--) {
			Message m = world.msgs.get(i);
			if (offsety % 2 == 0) {
				c = Main.getRainbow();
			} else {
				c = Main.getInverseRainbow();
			}
			main.font.setColor(c.r, c.g, c.b, (m.timer > 10 ? 1 : (m.timer / 10f)));
			main.font.draw(batch, ">", 37, 75 + offsety);
			main.font.setColor(1, 1, 1, (m.timer > 10 ? 1 : (m.timer / 10f)));
			main.font.draw(batch, m.msg, 50, 75 + offsety);
			offsety += 15;
		}
		main.font.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);
		int iter = 0;
		for (Effect e : world.effects) {
			if (e.hidden) continue;
			batch.draw(main.manager.get(AssetMap.get(e.texture), Texture.class), 0,
					Main.convertY(100 + (iter * 32)));
			main.font.draw(batch, e.getLocalizedTime(), 35, Main.convertY(100 + (iter * 32) - 15));
			main.font.draw(batch, e.name, 35, Main.convertY(100 + (iter * 32) - 30));
			iter++;
		}

		if (world.vignettecolour.a > 0f) {
			batch.setColor(world.vignettecolour);
			batch.draw(main.manager.get(AssetMap.get("vignette"), Texture.class), 0, 0,
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.setColor(Color.WHITE);
		}
		batch.setColor(1, 1, 1, 1);
		renderHealth();
		for (GearPart part : gearparts) {
			part.render(main);
		}
		batch.flush();

		GearPart item;
		for (int i = gearparts.size; --i >= 0;) {
			item = gearparts.get(i);
			if (item.lifetime <= 0) {
				gearparts.removeIndex(i);
			}
		}
	}

	public void renderHealth() {
		if (world.getPlayer() == null) return;
		batch.setColor(1, 1, 1, 0.1f);
		main.fillRect(0, 0, 16 + (world.getPlayer().maxhealth * 32), 56);
		for (int i = 0; i < world.getPlayer().maxhealth; i++) {
			batch.setColor(1, 1, 1, 1);
			if (i >= world.getPlayer().health) batch.setColor(0, 0, 0, 0.5f);
			Utils.drawRotated(batch, main.textures.get("gear"), 8 + (i * 32) - (i * 3),
					8 + (i % 2 != 0 ? 7 : 0), 32, 32,
					MathHelper.getNumberFromTime(System.currentTimeMillis(), 5f) * 360, i % 2 == 0);
			batch.setColor(1, 1, 1, 1);
		}
	}

	public void onDamagePlayer(int original) {
		for (int i = world.getPlayer().health; i < original; i++) {
			gearShatter(i);
		}

		world.vignettecolour.set(1, 0, 0, 1f);
	}

	private void gearShatter(int section) {
		float posx = 8 + (section * 32) - (section * 3);
		float posy = 8 + (section % 2 != 0 ? 7 : 0);

		int dividing = 4;

		for (int x = 0; x < 32 / 4; x++) {
			for (int y = 0; y < 32 / 4; y++) {
				if (Main.getRandomInst().nextBoolean()) {
					GearPart part = new GearPart();
					part.velox = Main.getRandomInst().nextFloat() - 0.5f;
					part.veloy = 3f;
					part.x = posx + x * dividing;
					part.y = posy + y * dividing;
					gearparts.add(part);
				}
			}
		}
	}

	public void renderDebug(int starting) {
		if (world.getPlayer() == null) return;
		main.font.setColor(Color.WHITE);

		main.font.draw(batch, "camerax: " + String.format("%.3f", world.camera.camerax) + " ("
				+ String.format("%.3f", world.camera.wantedx) + ")", 5, Main.convertY(starting));
		main.font.draw(batch, "cameray: " + String.format("%.3f", world.camera.cameray) + " ("
				+ String.format("%.3f", world.camera.wantedy) + ")", 5,
				Main.convertY(starting + 15));
		main.font.draw(batch, "posx: " + String.format("%.3f", world.getPlayer().x), 5,
				Main.convertY(starting + 30));
		main.font.draw(batch, "posy: " + String.format("%.3f", world.getPlayer().y), 5,
				Main.convertY(starting + 45));
		main.font.draw(batch, "velox: " + String.format("%.3f", world.getPlayer().velox), 5,
				Main.convertY(starting + 60));
		main.font.draw(batch, "veloy: " + String.format("%.3f", world.getPlayer().veloy), 5,
				Main.convertY(starting + 75));
		main.font.draw(batch, "bruteforcechecks: " + (world.entities.size * world.entities.size),
				5, Main.convertY(starting + 90));
		main.font.draw(batch, "entitycount: " + world.entities.size, 5,
				Main.convertY(starting + 105));
		main.font.draw(batch, "playerjump: " + world.getPlayer().jump, 5,
				Main.convertY(starting + 120));
		main.font.draw(batch, "time: " + Utils.formatMs(System.currentTimeMillis() - world.msTime),
				5, Main.convertY(starting + 135));
		main.font.draw(batch, "voidDistance: " + String.format("%.3f", world.getVoidDistance()), 5,
				Main.convertY(starting + 150));

	}

	private static class GearPart {

		float x, y;
		float velox, veloy;
		float lifetime = 1.5f;
		boolean tintWhite = (Main.getRandomInst().nextFloat() > 0.2f);

		public void render(Main main) {
			main.batch.setColor(1, 1, 1, 1);
			if (!tintWhite) main.batch.setColor(0, 0, 0, 1);
			main.fillRect(x, y, 4, 4);
			main.batch.setColor(1, 1, 1, 1);

			lifetime -= Gdx.graphics.getDeltaTime();
			x += velox;
			y += veloy;
			veloy -= 7.5f * Gdx.graphics.getDeltaTime();
		}
	}

}
