package stray;

import java.util.concurrent.TimeUnit;

import stray.conversation.Conversations;
import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.ui.Button;
import stray.ui.BackButton;
import stray.util.AssetMap;
import stray.util.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class LevelSelectScreen extends Updateable {

	public LevelSelectScreen(Main m) {
		super(m);
		container.elements.add(new BackButton(Gdx.graphics.getWidth() - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				goToMenu();
				return true;
			}
		});

		container.elements.add(new Button(Gdx.graphics.getWidth() / 2 - 80, 64, -1, -1,
				"menu.levelselect.enter") {

			@Override
			public boolean onLeftClick() {
				goToLevel();
				return true;
			}

			@Override
			public void render(Main main) {
				main.batch.setColor(Color.GRAY);
				main.fillRect(x - 8, y - 8, width + 16, height + 16);
				main.batch.setColor(Color.WHITE);
				super.render(main);
			}

			@Override
			public boolean visible() {
				return (getCurrent() >= 0 && getCurrent() < Levels.instance().levels.size());
			}
		});
	}

	float offset = 0;
	float velocity = 0;

	float wanted = 0;

	private final int DISTANCE = 512;

	@Override
	public void render(float delta) {
		main.batch.begin();
		main.batch.draw(main.manager.get(AssetMap.get("levelselectbg"), Texture.class), 0, 0,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		main.batch.setColor(Color.CYAN);
		main.fillRect(0, Gdx.graphics.getHeight() / 2 - 8, Gdx.graphics.getWidth(), 16);
		main.batch.setColor(Color.WHITE);

		main.font.setColor(Color.BLACK);
		for (int i = 0; i < Levels.instance().levels.size(); i++) {
			if (i < wanted - 3 || i > wanted + 3) continue;

			main.batch.draw(main.manager.get(AssetMap.get("levelselectdot"), Texture.class),
					(Gdx.graphics.getWidth() / 2 - 24) + ((i - offset) * DISTANCE),
					Gdx.graphics.getHeight() / 2 - 24, 48, 48);
			if (Levels.instance().levels.get(i) != null) {
				main.font.setScale(1.5f);
				main.drawCentered(
						Levels.getLevelName(i, Levels.LEVELS_PER_CHAPTER),
						(Gdx.graphics.getWidth() / 2) + ((i - offset) * DISTANCE),
						Gdx.graphics.getHeight() / 2 + 64);
				if (getCurrent() == i) {
					long millis = main.progress.getLong(Levels.instance().levels.get(i).name
							+ "-besttime", -1);
					long lastms = main.progress.getLong(Levels.instance().levels.get(i).name
							+ "-latesttime", -1);
					main.font.setScale(1);
					main.drawCentered(Translator.getMsg("menu.bestleveltime") + ": "
							+ (millis == -1 ? "--:--:--.---" : Utils.formatMs(millis)),
							Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 115);
					main.drawCentered(Translator.getMsg("menu.latestleveltime") + ": "
							+ (lastms == -1 ? "--:--:--.---" : Utils.formatMs(lastms)),
							Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 130);
					main.drawCentered("more info here later", Gdx.graphics.getWidth() / 2,
							Gdx.graphics.getHeight() / 2 - 145);

				}
			}
		}
		main.font.setScale(1);
		main.batch.setColor(getCurrent() == -1 ? Color.GRAY : (getCurrent() % 2 == 0 ? Main
				.getRainbow() : Main.getInverseRainbow()));
		main.batch.draw(main.manager.get(AssetMap.get("levelselected"), Texture.class),
				(Gdx.graphics.getWidth() / 2 - 32), Gdx.graphics.getHeight() / 2 - 96, 64, 128);
		main.batch.setColor(Color.WHITE);

		container.render(main);
		main.batch.end();

		if (Math.abs(wanted - offset) > 0.0001f) {
			velocity = (float) ((DISTANCE / 100f) * ((wanted - offset)));
			offset += velocity * Gdx.graphics.getDeltaTime();
		} else {
			offset = wanted;
			velocity = 0;
		}

	}

	public boolean moveNext() {
		if ((int) wanted + 1 < Levels.instance().levels.size()
				&& wanted < main.progress.getInteger("rightmostlevel", 0)) {
			wanted++;
			return true;
		}
		return false;
	}

	public boolean movePrev() {
		if ((int) wanted > 0) {
			wanted--;
			return true;
		}
		return false;
	}

	private void goToLevel() {
		if (getCurrent() >= 0 && getCurrent() < Levels.instance().levels.size()) {
			Main.BACKSTORY.prepare(
					Levels.instance().levels.get(getCurrent()).name,
					Gdx.files.internal("levels/" + Levels.instance().levels.get(getCurrent()).name
							+ ".xml"));
			if (Levels.instance().levels.get(getCurrent()).cutscene != null) {
				Main.CUTSCENE
						.prepare(Conversations.instance().convs.get(Levels.instance().levels
								.get(getCurrent()).cutscene), new FadeIn(), new FadeOut(),
								(Main.BACKSTORY));
				main.transition(new FadeIn(), null, Main.CUTSCENE);
			} else {
				main.transition(new FadeIn(), new FadeOut(), Main.BACKSTORY);
			}
		}
	}

	private void goToMenu() {
		main.setScreen(Main.MAINMENU);
	}

	public int getCurrent() {
		if ((offset >= wanted - (32f / DISTANCE) && offset <= wanted + (32f / DISTANCE))) {
			return (int) wanted;
		}
		return -1;
	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void renderDebug(int starting) {
		main.font.draw(main.batch, "wanted: " + wanted, 5, Main.convertY(starting));
		main.font.draw(main.batch, "offset: " + offset, 5, Main.convertY(starting + 15));
		main.font.draw(main.batch, "velocity: " + velocity, 5, Main.convertY(starting + 30));
		main.font
				.draw(main.batch, "getCurrent(): " + getCurrent(), 5, Main.convertY(starting + 45));
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
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
		if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			moveNext();
		} else if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			movePrev();
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			goToMenu();
		} else if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			goToLevel();
		}
	}

}
