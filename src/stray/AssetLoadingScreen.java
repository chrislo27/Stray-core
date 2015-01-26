package stray;

import java.util.Iterator;
import java.util.Map;

import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.util.AssetLogger;
import stray.util.render.Gears;
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

	private long startms = 0;
	
	@Override
	public void render(float delta) {
		main.manager.update(Math.round(1000f / Main.MAX_FPS));
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
			Main.logger.info("Finished loading all managed assets, took " + (System.currentTimeMillis() - startms) + " ms");
			
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
		startms = System.currentTimeMillis();
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
