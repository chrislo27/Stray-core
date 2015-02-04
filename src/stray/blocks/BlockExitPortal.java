package stray.blocks;

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
		Utils.drawRotatedCentered(
				world.batch,
				world.main.textures.get("gear"),
				x * world.tilesizex - world.camera.camerax + (World.tilesizey / 2f),
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey)
						+ (World.tilesizey / 2f),
				World.tilesizex * GearTransition.FULL_TO_MIDDLE_RATIO,
				World.tilesizey * GearTransition.FULL_TO_MIDDLE_RATIO,
				world.exitRotation, y % 2 == 0 && x % 2 == 0);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			if (!world.global.getString("completedLevel").equals("done!")) {
				world.global.setString("completedLevel", "done!");
				world.global.setInt("exitAnimation", ANIMATION_TIME);

				if (world.main.getScreen() != Main.GAME) return;
				save(world);
				Main.LEVELSELECT.moveNext();
			}
		}
		
		if (world.global.getInt("exitAnimation") >= 0) world.global.setInt("exitAnimation",
				world.global.getInt("exitAnimation") - 1);

		if (world.global.getInt("exitAnimation") < 0) {
			if (world.main.getScreen() != Main.GAME) return;
			world.main.transition(new FadeIn(), new FadeOut(), Main.LEVELSELECT);
		}
	}

	private void save(World world) {
		long lasttime = System.currentTimeMillis() - world.msTime;

		if (lasttime < world.main.progress.getLong(world.levelfile + "-besttime",
				Long.MAX_VALUE - 1)) {
			world.main.progress.putLong(world.levelfile + "-besttime", lasttime).flush();
		}

		world.main.progress.putLong(world.levelfile + "-latesttime", lasttime).flush();

		if (world.main.progress.getInteger("rightmostlevel", 0) == Main.LEVELSELECT.getCurrent()) {
			world.main.progress.putInteger("rightmostlevel",
					world.main.progress.getInteger("rightmostlevel", 0) + 1).flush();

		}
	}

}
