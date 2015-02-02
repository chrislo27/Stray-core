package stray;

import java.util.Iterator;
import java.util.Map;

import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.util.AssetLogger;
import stray.util.ScreenshotFactory;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Logger;

public class AssetLoadingScreen extends MiscLoadingScreen {

	public AssetLoadingScreen(Main m) {
		super(m);
		m.manager.setLogger(output);

	}

	private AssetLogger output = new AssetLogger("assetoutput", Logger.DEBUG);

	private long startms = 0;

	private boolean waitedAFrame = false;

	@Override
	public void render(float delta) {
		main.manager.update((int) (1000f / Main.MAX_FPS));
		do {
			if (main.manager.getProgress() >= 1f) {
				if(!waitedAFrame){
					waitedAFrame = true;
					break;
				}
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
				Main.logger.info("Finished loading all managed assets, took "
						+ (System.currentTimeMillis() - startms) + " ms");

				main.setScreen(Main.MAINMENU);
			}
		} while (false);

		super.render(delta);

		main.batch.begin();
		main.batch.setColor(1, 1, 1, 1);
		main.fillRect(Gdx.graphics.getWidth() / 2 - 128, Gdx.graphics.getHeight() / 2 - 10,
				256 * main.manager.getProgress(), 20);

		main.fillRect(Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() / 2 - 12, 260, 1);
		main.fillRect(Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() / 2 + 11, 260, 1);

		main.fillRect(Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() / 2 - 12, 1, 24);
		main.fillRect(Gdx.graphics.getWidth() / 2 + 132, Gdx.graphics.getHeight() / 2 - 12, 1, 24);

		if (main.manager.getAssetNames().size > 0) {
			main.drawTextBg(output.getLastMsg(),
					Gdx.graphics.getWidth() / 2
							- (main.font.getBounds(output.getLastMsg()).width / 2),
					Gdx.graphics.getHeight() / 2 - 35);
		}
		String percent = String.format("%.0f", (main.manager.getProgress() * 100f)) + "%";
		main.drawTextBg(percent, Gdx.graphics.getWidth() / 2
				- (main.font.getBounds(percent).width / 2), Gdx.graphics.getHeight() / 2 - 60);

//		if(Gdx.input.isKeyJustPressed(Keys.S)){
//			Texture tex = new Texture("images/blocks/portal/portal.png");
//			for(int i = 0; i < 32; i++){
//				Main.logger.debug("begin spiral " + (i + 1));
//				main.batch.setColor(1, 1, 1, 1);
//				Utils.drawRotated(main.batch, tex, 0 - (World.tilesizex / 2f),
//						(0) - (World.tilesizey / 2f),
//						tex.getWidth(), tex.getHeight(), i * 11.25f, true);
//				main.batch.flush();
//				Main.logger.debug("begin spiral screenshot" + (i + 1));
//				Pixmap pixmap = ScreenshotFactory.getScreenshot(0, 0, 64, 64, true);
//	            PixmapIO.writePNG(new FileHandle("spiral/number" + (i + 1) + ".png"), pixmap);
//	            pixmap.dispose();
//				Main.logger.debug("end spiral " + (i + 1));
//			}
//		}
		
		main.batch.end();
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
