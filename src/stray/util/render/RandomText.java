package stray.util.render;

import stray.Main;

import com.badlogic.gdx.Gdx;

public class RandomText {

	public static void render(Main main, String text, int chance, int maxrender) {
		if (text != null) {
			float height = main.font.getBounds(text).height;
			for (int i = 0; i < maxrender; i++) {
				if (Main.random(1, chance) != 1) continue;
				main.drawCentered(
						text,
						Main.random(1, Gdx.graphics.getWidth() - 1),
						Main.random(Math.round(height),
								Math.round(Gdx.graphics.getHeight() - height)));
			}
		}
	}

	/**
	 * synthetic method, turns chance into (10 + fps + (fps / 2))
	 * 
	 * @param main
	 * @param text
	 * @param maxrender
	 */
	public static void render(Main main, String text, int maxrender) {
		render(main, text,
				10 + Gdx.graphics.getFramesPerSecond() + (Gdx.graphics.getFramesPerSecond() / 2),
				maxrender);
	}

}
