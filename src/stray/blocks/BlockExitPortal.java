package stray.blocks;

import stray.Main;
import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.util.render.StencilMaskUtil;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BlockExitPortal extends Block {

	public BlockExitPortal(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		float a = world.batch.getColor().a;
		world.batch.setColor(Main.getRainbow(2.5f, 1));
		world.batch.setColor(world.batch.getColor().r, world.batch.getColor().g,
				world.batch.getColor().b, a);
		super.render(world, x, y);
		world.batch.setColor(Color.WHITE);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (world.main.getScreen() != Main.GAME) return;
		if (Block.entityIntersects(world, x, y, world.getPlayer())
				&& !world.global.getValue("completedLevel").equals("done!")) {
			world.global.setValue("completedLevel", "done!");
			long lasttime = System.currentTimeMillis() - world.msTime;

			if (lasttime < world.main.progress.getLong(world.levelfile + "-besttime",
					Long.MAX_VALUE - 1)) {
				world.main.progress.putLong(world.levelfile + "-besttime", lasttime).flush();
			}

			world.main.progress.putLong(world.levelfile + "-latesttime", lasttime).flush();

			if (world.main.progress.getInteger("rightmostlevel", 0) == Main.LEVELSELECT
					.getCurrent()) {
				world.main.progress.putInteger("rightmostlevel",
						world.main.progress.getInteger("rightmostlevel", 0) + 1).flush();

			}
			Main.LEVELSELECT.moveNext();

			world.main.transition(new FadeIn(), new FadeOut(), Main.LEVELSELECT);
		}
	}

}
