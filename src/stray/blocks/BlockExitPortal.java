package stray.blocks;

import stray.Levels;
import stray.Main;
import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;

public class BlockExitPortal extends Block {

	public BlockExitPortal(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		world.batch.setColor(Main.getRainbow(2.5f));
		super.render(world, x, y);
		world.batch.setColor(Color.WHITE);
	}

	@Override
	public void renderPlain(Main main, float camerax, float cameray, int x, int y, int magic) {
		main.batch.setColor(Main.getRainbow(2.5f));
		super.renderPlain(main, camerax, cameray, x, y, magic);
		main.batch.setColor(Color.WHITE);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if(world.main.getScreen() != Main.GAME) return;
		if (Block.entityIntersects(world, x, y, world.getPlayer())
				&& !world.global.getValue("completedLevel").equals("done!")) {
			world.global.setValue("completedLevel", "done!");
			long lasttime = System.currentTimeMillis() - world.time;

			if (lasttime < world.main.progress.getLong(world.levelfile + "-besttime", Long.MAX_VALUE - 1)) {
				world.main.progress.putLong(world.levelfile + "-besttime", lasttime)
						.flush();
			}
			
			world.main.progress.putLong(world.levelfile + "-latesttime", lasttime).flush();

			if (world.main.progress.getInteger("rightmostlevel", 0) == Main.LEVELSELECT.getCurrent()) {
				world.main.progress.putInteger("rightmostlevel",
						world.main.progress.getInteger("rightmostlevel", 0) + 1).flush();
				
			}
			Main.LEVELSELECT.moveNext();

			world.main.transition(new FadeIn(), new FadeOut(), Main.LEVELSELECT);
		}
	}

}
