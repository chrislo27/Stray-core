package stray;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class MainInputProcessor implements InputProcessor {

	Main main;

	public MainInputProcessor(Main m) {
		main = m;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (main.getConv() != null) {
			if (keycode == Keys.SPACE || keycode == Keys.ENTER) {
				if (main.currentConvText
						.equals(Translator.getMsg(main.getConv().getCurrent().text))) {
					if (main.getConv().next()) {
						main.setConv(null);
					} else {
						main.getConv().talk(main, 1 / 3f);
					}
					if (main.getScreen() instanceof CutsceneScreen) {
						((CutsceneScreen) main.getScreen()).onConvNext();
					}
					main.currentConvText = "";
				}else{
					main.currentConvText = Translator.getMsg(main.getConv().getCurrent().text);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (main.getScreen() != null) {
			if (button == Buttons.LEFT) {
				if (((Updateable) main.getScreen()).container.onLeftClick()) return true;
			} else if (button == Buttons.RIGHT) {
				if (((Updateable) main.getScreen()).container.onRightClick()) return true;
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (main.getScreen() == Main.LEVELEDITOR) {
			Main.LEVELEDITOR.blocksel += amount;
			if (Main.LEVELEDITOR.blocksel < 0) Main.LEVELEDITOR.blocksel = Main.LEVELEDITOR.blocks.size - 1;
			if (Main.LEVELEDITOR.blocksel >= Main.LEVELEDITOR.blocks.size) Main.LEVELEDITOR.blocksel = 0;
		}

		return false;
	}

}
