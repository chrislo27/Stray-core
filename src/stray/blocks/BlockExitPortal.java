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
		world.batch.end();
		
		StencilMaskUtil.prepareMask();
		world.main.shapes.begin(ShapeType.Filled);
		world.main.shapes.rect(x * world.tilesizex - world.camera.camerax,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey), World.tilesizex, World.tilesizey);
		world.main.shapes.end();
		
		world.batch.begin();
		StencilMaskUtil.useMask();
		float a = world.batch.getColor().a;
		world.batch.setColor(Main.getRainbow(2.5f, 1));
		world.batch.setColor(world.batch.getColor().r, world.batch.getColor().g,
				world.batch.getColor().b, a);
		Texture tex = world.main.manager.get(sprites.get("defaulttex"), Texture.class);
		Utils.drawRotated(world.batch, tex, x * (tex.getWidth() / 2) - world.camera.camerax,
				Main.convertY((y * (tex.getHeight() / 2) - world.camera.cameray) + World.tilesizey),
				tex.getWidth(), tex.getHeight(), World.tilesizex / 2f, World.tilesizey / 2f, 360 * MathHelper.getNumberFromTime(2.5f), true);
		world.batch.setColor(Color.WHITE);
		world.batch.flush();
		StencilMaskUtil.resetMask();
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
