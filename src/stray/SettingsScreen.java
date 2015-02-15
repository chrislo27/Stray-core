package stray;

import stray.ui.BackButton;
import stray.ui.BooleanButton;
import stray.ui.LanguageButton;
import stray.ui.Slider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class SettingsScreen extends Updateable {

	public SettingsScreen(Main m) {
		super(m);

		addGuiElements();
	}

	private void addGuiElements() {
		container.elements.clear();
		container.elements.add(new BackButton(Settings.DEFAULT_WIDTH - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				exitScreen();
				return true;
			}
		});
		container.elements.add(new LanguageButton(5, 5));
		container.elements.add(music.setSlider(Settings.musicVolume));
		container.elements.add(sound.setSlider(Settings.soundVolume));
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80, Gdx.graphics
				.getHeight() - 286, 160, 32, "menu.settings.showfps") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.showFPS = !Settings.showFPS;
				Settings.getPreferences()
						.putBoolean("showFPS", Settings.showFPS).flush();
				return true;
			}
		}.setState(Settings.showFPS));
		container.elements.add(debug);
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80, Gdx.graphics
				.getHeight() - 378, 160, 32, "menu.settings.vignette") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.showVignette = !Settings.showVignette;
				return true;
			}
		}.setState(Settings.showVignette));

	}

	private Slider music = new Slider((Settings.DEFAULT_WIDTH / 2) - 80,
			Gdx.graphics.getHeight() - 192, 160, 32);
	private Slider sound = new Slider((Settings.DEFAULT_WIDTH / 2) - 80,
			Gdx.graphics.getHeight() - 240, 160, 32);
	
	private BooleanButton debug = new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80, Gdx.graphics
			.getHeight() - 332, 160, 32, "menu.settings.debugmode") {

		@Override
		public boolean onLeftClick() {
			super.onLeftClick();
			Settings.debug = !Settings.debug;
			return true;
		}
	}.setState(Settings.debug);

	private boolean showRestartMsg = false;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		container.render(main);

		if (showRestartMsg) {
			main.font.setColor(1, 0, 0, 1);
			main.font.draw(main.batch, "[RED]*[]",
					(Settings.DEFAULT_WIDTH / 2) - 100 - (main.font.getSpaceWidth() * 2),
					Gdx.graphics.getHeight() - 144 + 20);

			main.font.setColor(1, 1, 1, 1);
			main.drawScaled(Translator.getMsg("menu.settings.requiresrestart"),
					(Settings.DEFAULT_WIDTH / 2), 100, 512, 0);
		}
		main.font.setColor(1, 1, 1, 1);
		main.font.draw(main.batch,
				Translator.getMsg("menu.settings.musicvol", (int) (music.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) + 80 + (main.font.getSpaceWidth()),
				Gdx.graphics.getHeight() - 192 + 20);
		main.font.draw(main.batch,
				Translator.getMsg("menu.settings.soundvol", (int) (sound.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) + 80 + (main.font.getSpaceWidth()),
				Gdx.graphics.getHeight() - 240 + 20);

		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.R) && Settings.debug) {
			addGuiElements();
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			exitScreen();
		}
		
		debug.setState(Settings.debug);
	}

	private void exitScreen() {
		main.setScreen(Main.MAINMENU);
		Settings.instance().save();
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
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

}
