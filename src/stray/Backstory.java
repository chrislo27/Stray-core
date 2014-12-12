package stray;

import stray.transition.FadeIn;
import stray.transition.FadeOut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Backstory extends MiscLoadingScreen {

	public Backstory(Main m) {
		super(m);
	}

	private String story = "";
	private float time = 0;
	private Updateable next;
	private FileHandle level;
	private boolean loaded = false;
	private final float TIMETAKEN = 1.5f;

	public void render(float delta) {
		super.render(delta);

		main.batch.begin();
		main.font.setColor(1, 1, 1, 1);
		if (story != null) main.font.drawWrapped(main.batch,
				Translator.getMsg("backstory." + story), Gdx.graphics.getWidth() / 4f,
				Main.convertY(Gdx.graphics.getHeight() * 0.4f), Gdx.graphics.getWidth() / 2f);

		if (time <= 0.25f) {
			main.font.setColor(1, 1, 1, 1f - (MathUtils.clamp(time, 0, 0.25f) * 4));
			main.drawCentered(Translator.getMsg("conversation.next"), Gdx.graphics.getWidth() / 2,
					Main.convertY(Gdx.graphics.getHeight() * 0.8f));
			main.font.setColor(1, 1, 1, 1);
			main.batch.setColor(Color.BLACK);
			main.fillRect(Gdx.graphics.getWidth() - 192, 0, 192, 128);
			main.batch.setColor(Color.WHITE);

		}
		main.batch.end();

		if (time > 0) {
			time -= Gdx.graphics.getDeltaTime();
			if (time < (TIMETAKEN / 2f) && !loaded) {
				loaded = true;
				Main.GAME.world.load(level);
			}
			if (time < 0) time = 0;
		}
	}

	public void prepare(String st, FileHandle level) {
		time = TIMETAKEN;
		story = st;
		next = Main.GAME;
		this.level = level;
		loaded = false;
	}

	public void renderUpdate() {
		super.renderUpdate();
		if (time <= 0) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				main.transition(new FadeIn(), new FadeOut(), next);
			}
		}

		
	}

}
