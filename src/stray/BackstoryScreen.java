package stray;

import stray.transition.FadeIn;
import stray.transition.FadeOut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class BackstoryScreen extends MiscLoadingScreen {

	public BackstoryScreen(Main m) {
		super(m);
	}

	private String story = "";
	private Updateable next;
	private FileHandle level;
	private boolean loaded = false;

	public void render(float delta) {
		super.render(delta);

		main.batch.begin();
		main.font.setColor(1, 1, 1, 1);
		if (story != null) main.font.drawWrapped(main.batch,
				Translator.getMsg("backstory." + story), Gdx.graphics.getWidth() / 4f,
				Main.convertY(Gdx.graphics.getHeight() * 0.4f), Gdx.graphics.getWidth() / 2f);

		main.font.setColor(1, 1, 1, 1);
		main.drawCentered(Translator.getMsg("conversation.next"), Gdx.graphics.getWidth() / 2,
				Main.convertY(Gdx.graphics.getHeight() * 0.8f));
		main.font.setColor(1, 1, 1, 1);
		main.batch.setColor(Color.BLACK);
		main.fillRect(Gdx.graphics.getWidth() - 192, 0, 192, 128);
		main.batch.setColor(Color.WHITE);

		main.batch.end();

		if (!loaded) {
			Main.GAME.world.load(level);
			loaded = true;
		}
	}

	public void prepare(String st, FileHandle level) {
		story = st;
		next = Main.GAME;
		this.level = level;
		loaded = false;
	}

	public void renderUpdate() {
		super.renderUpdate();
		if (loaded) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				main.transition(new FadeIn(), new FadeOut(), next);
			}
		}

	}

}
