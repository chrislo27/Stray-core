package stray.ui;

import stray.Main;
import stray.Translator;
import stray.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;


public abstract class ExitButton extends Button{

	public ExitButton(int x, int y) {
		super(x, y, 32, 32, null);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guiexit");
	}

	@Override
	public abstract boolean onLeftClick();
}
