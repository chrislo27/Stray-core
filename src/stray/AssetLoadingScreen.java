package stray;

import java.util.Iterator;
import java.util.Map;

import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.util.AssetLogger;
import stray.util.Gears;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Logger;

public class AssetLoadingScreen extends MiscLoadingScreen {

	public AssetLoadingScreen(Main m) {
		super(m);
		m.manager.setLogger(output);

	}

	private AssetLogger output = new AssetLogger("assetoutput", Logger.DEBUG);

	@Override
	public void render(float delta) {
		main.manager.update((int) (Main.MAX_FPS / 2f));
		if (main.manager.getProgress() >= 1f) {
			// finished
			for (String s : main.manager.getAssetNames()) {
				// System.out.println(s);
			}

			Iterator it = Blocks.instance().getAllBlocks();
			while (it.hasNext()) {
				Block block = (Block) ((Map.Entry) it.next()).getValue();
				block.postLoad(main);
			}

			Main.GAME.world = new World(main);

			main.setScreen(Main.MAINMENU);
		}
		super.render(delta);

	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void resize(int width, int height) {
		main.camera.setToOrtho(false, width, height);
		main.batch.setProjectionMatrix(main.camera.combined);
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

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void renderUpdate() {
	}

}
