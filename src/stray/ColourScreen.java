package stray;

import stray.util.AssetMap;
import stray.util.MathHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class ColourScreen extends Updateable {

	public ColourScreen(Main m) {
		super(m);
		randomize();
	}

	public static final int offx = (Settings.DEFAULT_WIDTH / 2) - 300;
	public static final int offy = (Gdx.graphics.getHeight() / 2) - 300;

	String[][] tiles = new String[10][7];
	int score = 0;

	int amount = MathUtils.random(3, 7);
	String choose = randomTile();

	int selx = 0;
	int sely = 0;

	String msg = "";

	int nuclearness = 0;

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		main.batch.begin();

		main.batch.draw(main.manager.get(AssetMap.get("colourmetal"), Texture.class), offx, offy);
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 7; y++) {
				main.batch.draw(
						main.manager.get(AssetMap.get("colour" + tiles[x][y]), Texture.class), offx
								+ (x * 60), Main.convertY(120 + (y * 60) + 60));
			}
		}
		main.batch.draw(main.manager.get(
				AssetMap.get("colourpointer"
						+ (MathHelper.getNumberFromTime() < 0.25f
								|| (MathHelper.getNumberFromTime() > 0.5f && MathHelper
										.getNumberFromTime() <= 0.75f) ? 1 : 2)), Texture.class),
				offx + (selx * 60), Main.convertY(120 + (sely * 60) + 60));

		main.font.setColor(Color.PURPLE);
		main.font.draw(main.batch, "SCORE: " + score, offx + 5, 150);
		main.font.setColor(Color.BLUE);
		main.font.draw(main.batch, msg, offx + 5, 135);
		main.font.setColor(Color.BLACK);
		main.font.draw(main.batch, "Current colour: " + choose + " / " + amount + " remaining",
				offx + 5, 120);
		main.font.setColor(Color.WHITE);
		main.drawCentered("Welcome to C.O.L.O.U.R.!", Settings.DEFAULT_WIDTH / 2, Main.convertY(5));
		main.drawCentered("This is a throwback to the first game ever made by chrislo27 in 2012.",
				Settings.DEFAULT_WIDTH / 2, Main.convertY(20));
		main.drawCentered("It is relatively pointless, but you found this easter egg nonetheless.",
				Settings.DEFAULT_WIDTH / 2, Main.convertY(35));
		main.drawCentered("WARNING: possible seizure warning with Apocalypse Swap",
				Settings.DEFAULT_WIDTH / 2, Main.convertY(50));
		main.font.setColor(Color.RED);
		main.font.draw(main.batch, "Arrow Keys - move | Spacebar - select", offx + 5, 105);
		main.font.draw(main.batch, "Escape - Main Menu | R - Apocalypse Swap", offx + 5, 90);

		if (nuclearness > 0) {
			if (nuclearness >= (41 - 15)) {
				main.batch.draw(main.manager.get(AssetMap.get("colournuclear"), Texture.class),
						offx, offy);
			} else {
				main.batch.setColor(Main.getRainbow());
				main.fillRect(offx, offy, 600, 600);
				main.batch.setColor(Color.WHITE);
			}
		}

		main.batch.end();

		if ((Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W))) {
			sely--;
			if (sely < 0) sely = 0;
		} else if ((Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.S))) {
			sely++;
			if (sely > 6) sely = 6;
		}
		if ((Gdx.input.isKeyJustPressed(Keys.LEFT) || Gdx.input.isKeyJustPressed(Keys.A))) {
			selx--;
			if (selx < 0) selx = 0;
		} else if ((Gdx.input.isKeyJustPressed(Keys.RIGHT) || Gdx.input.isKeyJustPressed(Keys.D))) {
			selx++;
			if (selx > 9) selx = 9;
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (tiles[selx][sely].equals(choose)) {
				amount--;
				score += 5;
				main.manager.get(AssetMap.get("colour200pts"), Sound.class).play(0.1f * Settings.soundVolume);
				if (amount <= 0) {
					msg = "You got them all!";
					String last = choose;
					while (choose.equals(last)) {
						choose = randomTile();
					}
					amount = MathUtils.random(3, 7);
				} else {
					msg = "Nice find!";
					switch (MathUtils.random(1, 3)) {
					case 1:
						msg = "Excellent!";
						break;
					case 2:
						msg = "Good work!";
						break;
					case 3:
						msg = "That's right!";
						break;
					}

				}

				String oldtile = tiles[selx][sely];
				while (tiles[selx][sely].equals(oldtile)) {
					tiles[selx][sely] = randomTile();
				}
			} else {
				msg = "That's not " + choose + "!";
				if (MathUtils.randomBoolean()) {
					msg = "This " + tiles[selx][sely] + " tile isn't " + choose + "!";
				}
				main.manager.get(AssetMap.get("colourincorrect"), Sound.class).play(0.5f * Settings.soundVolume);
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.R)) {
			if (nuclearness == 0) {
				nuclearness = 52;
				main.manager.get(AssetMap.get("colourswap"), Sound.class).play(0.333f * Settings.soundVolume);
			}
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.setScreen(Main.MAINMENU);
		}
	}

	public void randomize() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 7; y++) {
				tiles[x][y] = randomTile();
			}
		}
	}

	private String randomTile() {
		switch (MathUtils.random(1, 6)) {
		case 1:
			return "orange";
		case 2:
			return "yellow";
		case 3:
			return "green";
		case 4:
			return "purple";
		case 5:
			return "blue";
		case 6:
			return "red";
		default:
			return "orange";
		}
	}

	@Override
	public void tickUpdate() {
		if (nuclearness > 0) {
			nuclearness--;
			if (nuclearness == 0) {
				main.manager.get(AssetMap.get("colourcoverup"), Sound.class).play(0.5f);
				randomize();
			}
		}
	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// randomize();
		Gdx.graphics
				.setTitle("C.O.L.O.U.R. Legacy Edition - Colour Organization Laboratory Of Undeniable Realism");
	}

	@Override
	public void hide() {
		Main.getTitle();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void renderUpdate() {
	}

}
