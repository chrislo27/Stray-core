package stray.world;

import stray.Main;
import stray.Settings;
import stray.Translator;
import stray.blocks.Blocks;
import stray.entity.Entity;
import stray.util.AssetMap;
import stray.util.MathHelper;
import stray.util.Message;
import stray.util.ParticlePool;
import stray.util.Utils;
import stray.util.render.ElectricityRenderer;
import stray.util.render.SpaceBackground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class WorldRenderer {

	World world;
	SpriteBatch batch;
	Main main;

	private Sprite detection;

	public boolean showGrid = false;

	protected float timeSinceFull = 1337;

	public WorldRenderer(World world) {
		this.world = world;
		batch = world.batch;
		main = world.main;
	}

	public void renderBlocks() {
		main.font.setColor(Color.BLACK);
		float maxx = world.sizex;
		float maxy = world.sizey;
		int prex = (int) MathUtils.clamp(((world.camera.camerax / World.tilesizex) - 1), 0f, maxx);
		int prey = (int) MathUtils.clamp(((world.camera.cameray / World.tilesizey) - 1), 0f, maxy);
		int postx = (int) MathUtils.clamp((world.camera.camerax / World.tilesizex) + 2
				+ (Settings.DEFAULT_WIDTH / World.tilesizex), 0f, maxx);
		int posty = (int) MathUtils.clamp((world.camera.cameray / World.tilesizey) + 2
				+ (Settings.DEFAULT_HEIGHT / World.tilesizex), 0f, maxy);

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

	public void renderBackground() {
		if (world.background.equalsIgnoreCase("spacebackground")) {
			SpaceBackground.instance().render(main);
		} else {
			main.batch.draw(main.manager.get(AssetMap.get(world.background), Texture.class), 0, 0,
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	public void renderBuffer() {
		batch.setColor(0, 0, 0, 1);
		main.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setColor(1, 1, 1, 1);

		batch.draw(main.buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
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
		if (Main.random(1, 6) == 1) {
			float location = Main.random(1, Gdx.graphics.getHeight());
			ElectricityRenderer.drawP2PLightning(
					batch,
					((world.getVoidDistance() * World.tilesizex) - world.camera.camerax),
					location,
					((world.getVoidDistance() * World.tilesizex) - world.camera.camerax)
							+ Main.random(World.tilesizex * 1.75f, World.tilesizex * 2.75f),
					location + Main.random(-16, 16), 24, 1.5f, 3, 3, Colors.get("VOID_PURPLE")
							.toFloatBits());
		}

		int ylevel = Main.random(-World.tilesizex, Gdx.graphics.getHeight() + World.tilesizey);

		if (Main.random(1, 3) < 3) world.particles.add(ParticlePool
				.obtain()
				.setTexture("magnetglow")
				.setTint(0, 0, 0, 1)
				.setLifetime(1.25f)
				.setVelocity(
						-2,
						((ylevel > Gdx.graphics.getHeight() / 2f) ? MathUtils.clamp(
								-(((ylevel - (Gdx.graphics.getHeight() / 2f)) / (Gdx.graphics
										.getHeight() / 2f)) * 4), -4f, 4f) : MathUtils.clamp(
								4 - ((ylevel / (Gdx.graphics.getHeight() / 2f)) * 4), -4f, 4f)))
				.setPosition((((world.getVoidDistance())) + 2 + Main.random(0.5f, 1.5f)),
						(world.camera.cameray + ylevel) / World.tilesizey));
	}

	public void renderEntities() {

		for (Entity e : world.entities) {
			e.render(Gdx.graphics.getDeltaTime());
			if (Settings.debug) {
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

		if (world.vignettecolour.a > 0f) {
			batch.setColor(world.vignettecolour);
			batch.draw(main.manager.get(AssetMap.get("vignette"), Texture.class), 0, 0,
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.setColor(Color.WHITE);
		}
		batch.setColor(1, 1, 1, 1);

		if (detection == null) {
			detection = new Sprite(main.manager.get(AssetMap.get("detectionarrow"), Texture.class));
			detection.setOriginCenter();
		}

		renderHealth();

		renderAugments();
		batch.flush();
	}

	public void renderHealth() {
		if (world.getPlayer() == null) return;
		if (world.getPlayer().health >= world.getPlayer().maxhealth) {
			if (timeSinceFull >= 3 && world.timeWithoutInput < 5) return;
			timeSinceFull += Gdx.graphics.getRawDeltaTime();
			if (Math.round(timeSinceFull * 5) % 2 == 0 && timeSinceFull < 1.5f) {
				return;
			}
		}

		batch.setColor(0, 0, 0, 0.3f);
		main.fillRect(0, 0, 128, 50);

		main.font.setScale(0.8f);
		float baroffset = main.font.getBounds(Translator.getMsg("ui.heat")).width - 4;
		main.font.setColor(1 * (1 - (world.getPlayer().health / world.getPlayer().maxhealth)),
				1 * (world.getPlayer().health / world.getPlayer().maxhealth),
				1 * (world.getPlayer().health / world.getPlayer().maxhealth), 1);
		main.font.draw(batch, Translator.getMsg("ui.heat"), 4, 20);
		main.font.setColor(1, 1, 1, 1);
		main.font.setScale(1);

		batch.setColor(1 * (1 - (world.getPlayer().health / world.getPlayer().maxhealth)), 0,
				1 * (world.getPlayer().health / world.getPlayer().maxhealth), 0.25f);
		main.fillRect(8 + baroffset, 8, 112 - baroffset, 12);

		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
		main.fillRect(8 + baroffset, 8,
				(112 - baroffset) * (1 - (world.getPlayer().health / world.getPlayer().maxhealth)),
				12);

		batch.setColor(1, 1, 1, 1);

		if (world.getPlayer().health / world.getPlayer().maxhealth < 0.25f) {
			main.font.setColor(Color.RED);
			main.drawScaled(Translator.getMsg("ui.overheat"), 64, 40, 128, 4);
		} else if (world.getPlayer().health / world.getPlayer().maxhealth < 1) {
			main.font.setColor(Color.RED);
			main.drawScaled(Translator.getMsg("ui.hpwarning"), 64, 40, 128, 4);
		} else {
			main.font.setColor(Color.GREEN);
			main.drawScaled(Translator.getMsg("ui.systemsnominal"), 64, 40, 128, 4);
		}

		main.font.setColor(Color.WHITE);
	}

	public void onDamagePlayer(float original) {
		timeSinceFull = 0;
		world.vignettecolour.set(1, 0, 0, 1f);
	}

	public void renderAugments() {
		for (int i = 0; i < 5; i++) {
			Utils.drawRotated(batch, main.textures.get("gear"), 8 + (i * 32) - (i * 3),
					128 + (i % 2 != 0 ? 7 : 0), 32, 32,
					MathHelper.getNumberFromTime(((MathHelper.getNumberFromTime(2.5f) > 0.65f) ? 0.75f : 5f)) * 360, i % 2 == 0);
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

}
