package stray.ui;

import stray.Main;
import stray.Translator;
import stray.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Button implements GuiElement {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	public String text;

	/**
	 * set width and/or height to -1 to set default (160, 32)
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public Button(int x, int y, int width, int height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;

		if (width == -1) this.width = 160;
		if (height == -1) this.height = 32;
	}

	public void imageRender(Main main, String img) {
		main.batch.draw(main.manager.get(AssetMap.get(img), Texture.class), x, y, width, height);
		if (Gdx.input.getX() >= x && Gdx.input.getX() <= x + width
				&& Main.convertY(Gdx.input.getY()) >= y
				&& Main.convertY(Gdx.input.getY()) <= y + height) {
			main.batch.setColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 0.42f);
			main.batch
					.draw(main.manager.get(AssetMap.get(img), Texture.class), x, y, width, height);
			main.batch.setColor(Color.WHITE);
		}
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guibg");
		main.font.setColor(Color.BLACK);
		main.drawCentered(Translator.getMsg(text), x + (width / 2),
				y + (height / 2) + (main.font.getBounds(text).height / 2));
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean onLeftClick() {
		return false;
	}

	@Override
	public boolean onRightClick() {
		return false;
	}

	@Override
	public boolean visible() {
		return true;
	}

}
