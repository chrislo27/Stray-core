package stray.world;

import stray.Main;
import stray.Settings;
import stray.Translator;
import stray.augment.Augments;
import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.entity.Entity;
import stray.util.AssetMap;
import stray.util.MathHelper;
import stray.util.ParticlePool;
import stray.util.Utils;
import stray.util.render.ElectricityRenderer;
import stray.util.render.PostProcessing;
import stray.util.render.SpaceBackground;
import stray.util.render.StencilMaskUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class WorldRenderer {

	World world;
	SpriteBatch batch;
	Main main;

	private Sprite detection;

	public boolean showGrid = false;

	protected long lastAugmentSwitch = 0;

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
		for (int level = 0; level < 2; level++) {
			for (int x = prex; x < postx; x++) {
				for (int y = prey; y < posty; y++) {
					if (world.getBlock(x, y).isRenderedFront() != (level != 0)) continue;
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
			if (level == 0) {
				renderEntities();
			}
		}

	}

	public void renderBackground() {
		if (world.background.equalsIgnoreCase("spacebackground")) {
			SpaceBackground.instance().render(main);
		} else {
			main.batch.draw(main.manager.get(AssetMap.get(world.background), Texture.class), 0, 0,
					Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight());
		}
	}

	public void renderBuffer() {
		batch.setColor(0, 0, 0, 1);
		main.fillRect(0, 0, Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight());
		batch.setColor(1, 1, 1, 1);

		batch.draw(main.buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Settings.DEFAULT_WIDTH, -Gdx.graphics.getHeight());
	}

	public void renderVoid() {
		batch.setColor(0, 0, 0, 1);
		main.fillRect(0, 0, ((world.getVoidDistance() * World.tilesizex) - world.camera.camerax),
				Gdx.graphics.getHeight());
		batch.draw(main.manager.get(AssetMap.get("voidend"), Texture.class),
				((world.getVoidDistance() * World.tilesizex) - world.camera.camerax), 0,
				main.manager.get(AssetMap.get("voidend"), Texture.class).getWidth(),
				Gdx.graphics.getHeight());

		int ylevel = MathUtils.random(-World.tilesizex, Gdx.graphics.getHeight() + World.tilesizey);

		if (MathUtils.random(1, 3) < 3) world.particles.add(ParticlePool
				.obtain()
				.setTexture("magnetglow")
				.setTint(batch.getColor())
				.setLifetime(1.25f)
				.setVelocity(
						-2,
						((ylevel > Gdx.graphics.getHeight() / 2f) ? MathUtils.clamp(
								-(((ylevel - (Gdx.graphics.getHeight() / 2f)) / (Gdx.graphics
										.getHeight() / 2f)) * 4), -4f, 4f) : MathUtils.clamp(
								4 - ((ylevel / (Gdx.graphics.getHeight() / 2f)) * 4), -4f, 4f)))
				.setPosition((((world.getVoidDistance())) + 2 + MathUtils.random(0.5f, 1.5f)),
						(world.camera.cameray + ylevel) / World.tilesizey));

		batch.setColor(1, 1, 1, 1);
		if (MathUtils.random(1, 6) == 1) {
			float location = MathUtils.random(1, Gdx.graphics.getHeight());
			ElectricityRenderer.drawP2PLightning(batch,
					((world.getVoidDistance() * World.tilesizex) - world.camera.camerax), location,
					((world.getVoidDistance() * World.tilesizex) - world.camera.camerax)
							+ MathUtils.random(World.tilesizex * 0.75f, World.tilesizex * 2.75f),
					location + MathUtils.random(-64, 64), 24, 1.5f, 3, 3, Colors.get("VOID_PURPLE")
							.toFloatBits());
		}
	}

	private void renderEntities() {
		for (Entity e : world.entities) {
			if (!world.isEntityOnScreen(e)) {
				continue;
			}
			e.render(Gdx.graphics.getDeltaTime());
		}
		batch.flush();
	}

	public void tickUpdate() {

	}

	private long fireStart = 0;

	public void renderUi() {
		if (world.getPlayer() == null) return;

		main.font.setColor(Color.WHITE);
		batch.setColor(1, 1, 1, 1);

		if (Settings.showVignette) {
			batch.setColor(0, 0, 0, 0.25f);
			batch.draw(main.manager.get(AssetMap.get("vignette"), Texture.class), 0, 0,
					Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight());
			batch.setColor(Color.WHITE);
		}

		if (world.vignettecolour.a > 0f) {
			batch.setColor(world.vignettecolour);
			batch.draw(main.manager.get(AssetMap.get("vignette"), Texture.class), 0, 0,
					Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight());
			batch.setColor(Color.WHITE);
		}

		if (world.getPlayer().fireTime > 0) {
			batch.setColor(1, 1, 1, 0.333333333f);
			batch.draw(main.animations.get("fire-hud").getCurrentFrame(), 0,
					Gdx.graphics.getHeight(), Settings.DEFAULT_WIDTH, -Gdx.graphics.getHeight());
		} else {
			if (fireStart != 0) fireStart = 0;
		}

		if (detection == null) {
			detection = new Sprite(main.manager.get(AssetMap.get("detectionarrow"), Texture.class));
			detection.setOriginCenter();
		}

		renderHealth();

		renderAugments();

		renderPlayerArrow();
	}

	public void renderPlayerArrow() {
		if (!world.isEntityOnScreen(world.getPlayer())) {
			float x = world.getPlayer().x;
			float y = world.getPlayer().y;
			float clampedX = MathUtils.clamp((x * World.tilesizex) - world.camera.camerax, 0,
					Settings.DEFAULT_WIDTH - (world.getPlayer().sizex * World.tilesizex));
			float clampedY = MathUtils.clamp(
					Main.convertY((y * World.tilesizey) - world.camera.cameray), 0,
					Gdx.graphics.getHeight() - (world.getPlayer().sizey * World.tilesizey));

			batch.setColor(1, 1, 1, (MathHelper.clampNumberFromTime(0.75f)));

			world.getPlayer().renderSelf(clampedX, clampedY);

			Texture arrow = main.manager.get(AssetMap.get("playerdirectionarrow"), Texture.class);

			batch.setColor(1, 1, 1, 1);
			float degrees = MathHelper.calcRotationAngleInDegrees((x * World.tilesizex)
					- world.camera.camerax + arrow.getWidth() / 2,
					Main.convertY((y * World.tilesizey) - world.camera.cameray) + arrow.getHeight()
							/ 2, clampedX + arrow.getWidth() / 2, clampedY - arrow.getHeight() / 2);

			Utils.drawRotated(batch, arrow, clampedX, clampedY - arrow.getHeight(),
					arrow.getWidth(), arrow.getHeight(), degrees, true);
		}
	}

	private float originalhealth = -1;
	private float timeUntilOrig = 0;

	public void renderHealth() {
		if (world.getPlayer() == null) return;
		if (originalhealth <= -1) originalhealth = world.getPlayer().health;
		if (timeUntilOrig > 0) {
			timeUntilOrig -= Gdx.graphics.getDeltaTime();
		}
		if (timeUntilOrig <= 0) {
			timeUntilOrig = 0;
			originalhealth = MathUtils.clamp(
					originalhealth
							+ ((world.getPlayer().health - originalhealth)
									* Gdx.graphics.getDeltaTime() * 8f), 0f,
					world.getPlayer().maxhealth);
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

		// the background of the bar
		batch.setColor(1 * (1 - (world.getPlayer().health / world.getPlayer().maxhealth)), 0,
				1 * (world.getPlayer().health / world.getPlayer().maxhealth), 0.25f);
		main.fillRect(8 + baroffset, 8, 112 - baroffset, 12);

		// actual health
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.25f);
		main.fillRect(8 + baroffset, 8,
				(112 - baroffset) * (1 - (world.getPlayer().health / world.getPlayer().maxhealth)),
				12);

		// opaque health
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
		main.fillRect(8 + baroffset, 8,
				(112 - baroffset) * (1 - (originalhealth / world.getPlayer().maxhealth)), 12);

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
		originalhealth = original;
		timeUntilOrig = 0.5f;
		world.vignettecolour.set(1, 0, 0, 1f);
	}

	public void renderAugments() {
		int augments = world.getAugmentsUnlocked();

		if (augments <= 0) return;

		batch.setColor(0, 0, 0, 0.3f);
		main.fillRect(128, 0, 16 + (augments * 29), 50);
		batch.setColor(1, 1, 1, 1);

		for (int i = 0; i < augments; i++) {
			batch.setColor(0, 0, 0, 0.5f);
			if (i == world.currentAugment) batch.setColor(Augments.getAugment(world.currentAugment)
					.getColor());
			if (i == 3) batch.setColor(1, 0, 0, 1);
			Utils.drawRotated(batch, main.textures.get("gear"), 135 + (i * 32) - (i * 3),
					5 + (i % 2 != 0 ? 7 : 0), 32, 32,
					MathHelper.getNumberFromTime(((world.augmentActivate) ? 0.75f : 5f)) * 360,
					i % 2 == 0);
		}
		batch.setColor(1, 1, 1, 1);

		if (System.currentTimeMillis() - lastAugmentSwitch <= 2500) {
			long timeSinceSwitch = (System.currentTimeMillis() - lastAugmentSwitch);
			float alpha = ((timeSinceSwitch > 2500 - 250) ? ((250 - (timeSinceSwitch - (2500 - 250))) / 250f)
					: 1);
			main.font.setColor(1, 1, 1, alpha);
			batch.setColor(1, 1, 1, alpha);
			String text = Translator.getMsg("ui.currentaugment")
					+ Translator.getMsg(Augments.getAugment(world.currentAugment).getName());
			main.drawTextBg(text, Settings.DEFAULT_WIDTH / 2
					- (main.font.getBounds(text).width / 2), 50);
		}
		batch.setColor(1, 1, 1, 1);
		main.font.setColor(1, 1, 1, 1);

		Augments.getAugment(world.currentAugment).renderUi(main, world);
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
		main.font.draw(batch, "deaths: " + world.deaths.size, 5, Main.convertY(starting + 165));

	}

}
