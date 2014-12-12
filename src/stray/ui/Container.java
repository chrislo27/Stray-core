package stray.ui;

import stray.Main;
import stray.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Container {

	public Array<GuiElement> elements = new Array<GuiElement>();

	public void render(Main main) {
		for (GuiElement e : elements) {
			if (!e.visible()) continue;
			e.render(main);
		}
	}

	public boolean onLeftClick() {
		for (GuiElement e : elements) {
			if (!e.visible()) continue;
			if (Gdx.input.getX() >= e.getX() && Gdx.input.getX() <= e.getX() + e.getWidth()) {
				if (Main.convertY(Gdx.input.getY()) >= e.getY()
						&& Main.convertY(Gdx.input.getY()) <= e.getY() + e.getHeight()) {
					if (e.onLeftClick()) return true;
				}
			}
		}

		return false;
	}

	public boolean onRightClick() {
		for (GuiElement e : elements) {
			if (!e.visible()) continue;
			if (Gdx.input.getX() >= e.getX() && Gdx.input.getX() <= e.getX() + e.getWidth()) {
				if (Main.convertY(Gdx.input.getY()) >= e.getY()
						&& Main.convertY(Gdx.input.getY()) <= e.getY() + e.getHeight()) {
					if (e.onRightClick()) return true;
				}
			}
		}

		return false;
	}
}
