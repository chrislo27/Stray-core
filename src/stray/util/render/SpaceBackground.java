package stray.util.render;

import stray.Main;
import stray.blocks.Blocks;
import stray.transition.Eat;
import stray.transition.FadeOut;
import stray.transition.Spiral;
import stray.util.AssetMap;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class SpaceBackground {

	private static SpaceBackground instance;

	private SpaceBackground() {
	}

	public static SpaceBackground instance() {
		if (instance == null) {
			instance = new SpaceBackground();
			instance.loadResources();
		}
		return instance;
	}

	private int magicnumber = Main.getRandomInst().nextInt();
	private float camerax = 0;
	private double timewatched = 0d;
	private float krakenx = Gdx.graphics.getWidth();
	private Array<VisualAsteroid> asteroids = new Array<VisualAsteroid>();

	private int cooldown = 0;

	public static int maxAsteroids = 16;

	private void loadResources() {
		while (asteroids.size < maxAsteroids) {
			VisualAsteroid ast = new VisualAsteroid();
			moveAsteroidToEdge(ast);
			ast.x = World.tilesizex + (Gdx.graphics.getWidth() * Main.getRandomInst().nextFloat());
			asteroids.add(ast);
		}
	}

	public void render(Main main) {
		if (asteroids.size < maxAsteroids) {
			VisualAsteroid ast = new VisualAsteroid();
			moveAsteroidToEdge(ast);
			asteroids.add(ast);
		}

		for (int x = Math.max(Math.round(camerax / World.tilesizex - 1), 0); x < (((Gdx.graphics
				.getWidth() + camerax) / World.tilesizex) + 1); x++) {
			for (int y = 0; y < ((Gdx.graphics.getHeight() / World.tilesizey) + 2); y++) {
				Blocks.instance().getBlock("space")
						.renderPlain(main, camerax, 0, x, y, magicnumber);
			}
		}
		camerax = camerax + (World.tilesizex / 2) * Gdx.graphics.getRawDeltaTime();

		main.font.setColor(Color.WHITE);

		for (int i = asteroids.size - 1; i > -1; i--) {
			// main.drawCentered("||||||||||||||||||||||||||||||||||||||||",
			// asteroids.get(i).x, asteroids.get(i).y - 15);
			asteroids.get(i).renderPlain(main);

			asteroids.get(i).x -= (asteroids.get(i).speed * World.tilesizex)
					* Gdx.graphics.getRawDeltaTime();

			if (asteroids.get(i).x < -(32 * asteroids.get(i).size)) {
				if (asteroids.size > maxAsteroids
						&& (getAsteroidsRight(Gdx.graphics.getWidth() / 2) > maxAsteroids)) {
					asteroids.removeIndex(i);
				} else {
					moveAsteroidToEdge(asteroids.get(i));
				}
			}
		}

		main.batch.draw(main.manager.get(AssetMap.get("spacekraken"), Texture.class), krakenx, 0,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		main.font.draw(main.batch, "Did you ever really remove me?", krakenx + 100, 225);

		if (Main.debug) {
			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				maxAsteroids++;
				msg = "# of asteroids now: " + maxAsteroids;
				msgtime = 3;
			} else if (Gdx.input.isKeyJustPressed(Keys.DOWN) && maxAsteroids > 0) {
				maxAsteroids--;
				msg = "# of asteroids now: " + maxAsteroids;
				msgtime = 3;
			}
		}

		main.font.setColor(1, 1, 1, (msgtime <= 0.5f ? msgtime * 2f : 1));
		main.font.draw(main.batch, msg, 5, 35);
		if (msgtime > 0) {
			msgtime -= Gdx.graphics.getDeltaTime();
			if (msgtime <= 0) {
				msgtime = 0;
				msg = "";
			}
		}

		if (Gdx.input.isButtonPressed(Buttons.LEFT) && cooldown == 0) {
			if (krakenx < Gdx.graphics.getWidth() && krakenx > -Gdx.graphics.getWidth()) {
				main.awardAchievement("kraken");
			} else {
				for (VisualAsteroid ast : asteroids) {
					if (Gdx.input.getX() >= ast.x && Gdx.input.getX() <= (ast.x + (ast.size * 32))) {
						if (Gdx.input.getY() <= ast.y
								&& Gdx.input.getY() >= (ast.y - (ast.size * 32))) {
							if (ast.colour) {
								main.awardAchievement("secret");
								ast.colour = false;
								cooldown = (int) (Gdx.graphics.getFramesPerSecond() * 1.5f);
								main.transition(new Spiral(3), new FadeOut(), Main.COLOUR);

								return;
							}
						}
					}
				}
			}
		}

		if (cooldown > 0) cooldown--;
		timewatched += Gdx.graphics.getRawDeltaTime();
		if (timewatched >= (1800) && krakenx > -Gdx.graphics.getWidth()) {
			krakenx -= Gdx.graphics.getDeltaTime() * 64;
		}
	}

	private int getAsteroidsRight(int rightof) {
		tmp = 0;
		for (VisualAsteroid ast : asteroids) {
			if (ast.x >= rightof) tmp++;
		}
		return tmp;
	}

	private int tmp = 0;
	private String msg = "";
	private float msgtime = 0;

	private void moveAsteroidToEdge(VisualAsteroid ast) {
		ast.y = (Gdx.graphics.getHeight() - (ast.size * (32 / 4)))
				* Main.getRandomInst().nextFloat();
		ast.x = Gdx.graphics.getWidth() + World.tilesizex
				+ (Main.getRandomInst().nextFloat() * (World.tilesizex * 2));
		ast.prepare();
	}

	public boolean containsSpecial() {
		for (VisualAsteroid a : asteroids) {
			if (a.colour) return true;
		}
		return false;
	}

}

class VisualAsteroid {

	float x = 0;
	float y = 0;
	float size, speed;
	boolean colour = false;

	public VisualAsteroid() {
		prepare();
	}

	public void prepare() {
		size = 0.75f + Main.getRandomInst().nextFloat();
		speed = 0.75f + Main.getRandomInst().nextFloat();
		colour = false;
		if ((Main.random(1, Math.round(SpaceBackground.maxAsteroids * 32)) == 1)
				&& !SpaceBackground.instance().containsSpecial()) {
			colour = true;
		}
	}

	public void renderPlain(Main main) {
		if (!colour) {
			main.batch.draw(main.manager.get(AssetMap.get("smallasteroid"), Texture.class), x,
					Main.convertY(y), 32 * size, 32 * size);
		} else {
			Utils.drawTexWithShiny(main,
					main.manager.get(AssetMap.get("smallasteroid"), Texture.class), x,
					Main.convertY(y), 32 * size, 32 * size);
		}
	}
}
