package stray;

import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.ui.BooleanButton;
import stray.ui.BackButton;
import stray.ui.Button;
import stray.ui.ChoiceButton;
import stray.ui.LanguageButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class SettingsScreen extends Updateable {

	public SettingsScreen(Main m) {
		super(m);

		addGuiElements();
	}

	private void addGuiElements() {
		container.elements.clear();
		container.elements.add(new BackButton(Gdx.graphics.getWidth() - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.MAINMENU);
				return true;
			}
		});
		container.elements.add(new LanguageButton(5, 5));
		container.elements.add(new BooleanButton((Gdx.graphics.getWidth() / 2) - 100, Gdx.graphics
				.getHeight() - 128, 200, 32, "menu.settings.resolution") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.getPreferences()
						.putBoolean("resolutionsmall", !Settings.isSmallResolution()).flush();
				showRestartMsg = true;
				return true;
			}
		}.setState(Settings.isSmallResolution()));

	}

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
					(Gdx.graphics.getWidth() / 2) - 100 - (main.font.getSpaceWidth() * 2),
					Gdx.graphics.getHeight() - 128 + 20);

			main.font.setColor(1, 1, 1, 1);
			main.drawScaled(Translator.getMsg("menu.settings.requiresrestart"),
					(Gdx.graphics.getWidth() / 2), 100, 512, 0);
		}

		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.R) && Main.debug) {
			addGuiElements();
		}
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
