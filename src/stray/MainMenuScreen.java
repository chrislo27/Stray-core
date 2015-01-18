package stray;

import stray.conversation.Conversations;
import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.ui.Button;
import stray.ui.BackButton;
import stray.ui.SettingsButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public strictfp class MainMenuScreen extends Updateable {

	public MainMenuScreen(Main m) {
		super(m);

		container.elements.add(new Button((Gdx.graphics.getWidth() / 2) - 80, 64, 160, 32,
				"menu.new") {

			@Override
			public boolean onLeftClick() {
				Main.CUTSCENE.prepare(Conversations.instance().convs.get("dev"), new FadeIn(),
						new FadeOut(), Main.NEWGAME);
				main.transition(new FadeIn(Color.BLACK, 0.5f), null, Main.CUTSCENE);

				Runtime.getRuntime().gc();
				return true;
			}
		});
		container.elements.add(new Button((Gdx.graphics.getWidth() / 2) - 80, 128, 160, 32,
				"menu.continue") {

			@Override
			public boolean onLeftClick() {
				if (hasSave) {
					main.transition(new FadeIn(Color.BLACK, 0.5f), new FadeOut(Color.BLACK, 0.5f),
							Main.LEVELSELECT);

					Runtime.getRuntime().gc();
				}
				return true;
			}

			@Override
			public boolean visible() {
				return hasSave;
			}
		});
		container.elements.add(new SettingsButton(5, 5));
		container.elements.add(new BackButton(Gdx.graphics.getWidth() - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				Gdx.app.exit();
				System.exit(0);
				return true;
			}
		});
	}

	boolean hasSave = false;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();

		main.font.setColor(Color.WHITE);
		main.font.setScale(2.5f);
		main.drawCentered(Translator.getMsg("gamename").toUpperCase(), Gdx.graphics.getWidth() / 2,
				Main.convertY(200));
		main.font.setScale(1);

		main.drawInverse(Main.version, Gdx.graphics.getWidth() - 5, 20);
		if (!Main.latestVersion.equals("")) {
			if (Main.latestVersionNumber > Main.currentVersionNumber) {
				main.font.setColor(1, 0, 0, 1);
				main.drawInverse(Translator.getMsg("menu.newversion") + Main.latestVersion,
						Gdx.graphics.getWidth() - 5, 35);
				main.font.setColor(1, 1, 1, 1);
			} else if (Main.latestVersionNumber == Main.currentVersionNumber) {
				main.font.setColor(0, 1, 0, 1);
				main.drawInverse(Translator.getMsg("menu.uptodate"), Gdx.graphics.getWidth() - 5,
						35);
				main.font.setColor(1, 1, 1, 1);
			} else if (Main.latestVersionNumber < Main.currentVersionNumber) {
				main.font.setColor(0, 0, 1, 1);
				main.drawInverse(Translator.getMsg("menu.versionahead") + Main.latestVersion + ")",
						Gdx.graphics.getWidth() - 5, 35);
				main.font.setColor(1, 1, 1, 1);
			}
		} else {
			main.drawInverse(Translator.getMsg("menu.checkingversion"),
					Gdx.graphics.getWidth() - 5, 35);
		}
		container.render(main);
		main.font.setColor(Color.WHITE);
		main.batch.setColor(1, 1, 1, 1);

		main.batch.end();
	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		hasSave = main.getPref("settings").getBoolean("saveExists", false);
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
	public void renderDebug(int starting) {
	}

	@Override
	public void renderUpdate() {

	}

}
