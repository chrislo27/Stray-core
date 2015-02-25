package stray.blocks;

import stray.Levels;
import stray.Main;
import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.transition.GearTransition;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class BlockExitPortal extends Block {

	public BlockExitPortal() {
		super(null);
	}

	public static final int ANIMATION_TIME = Math.round(Main.TICKS * 5f);

	@Override
	public void render(World world, int x, int y) {
		super.render(world, x, y);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			if (!world.global.getString("completedLevel").equals("done!")) {
				world.global.setString("completedLevel", "done!");

				if (world.main.getScreen() != Main.GAME) return;
				save(world);
				Main.LEVELSELECT.moveNext();
				world.main.transition(new FadeIn(), null, Main.RESULTS.setData(world.levelfile,
						Levels.instance().getNumFromLevelFile(world.levelfile), world.voidTime > 0,
						world.deaths));
			}
		}

	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}

	private void save(World world) {
		long lasttime = System.currentTimeMillis() - world.msTime;

		if (lasttime < world.main.progress.getLong(world.levelfile + "-besttime",
				Long.MAX_VALUE - 1)) {
			world.main.progress.putLong(world.levelfile + "-besttime", lasttime);
		}

		world.main.progress.putLong(world.levelfile + "-latesttime", lasttime);

		if (world.main.progress.getInteger("rightmostlevel", 0) == Main.LEVELSELECT.getCurrent()) {
			world.main.progress.putInteger("rightmostlevel",
					world.main.progress.getInteger("rightmostlevel", 0) + 1);

		}

		world.main.progress.flush();
	}

}
