package stray.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import stray.Main;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.world.World;

public class BlockPlatform extends Block {

	public BlockPlatform(String path) {
		super(path);
	}

	@Override
	public int isSolid(World world, int x, int y) {
		return MathUtils.clamp(world.getMeta(x, y), SolidFaces.NONE, SolidFaces.ALL);
	}

	@Override
	public void renderWithOffset(World world, int x, int y, float offx, float offy) {
		if (isSolid(world, x, y) <= 0) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, World.tilesizex / 2f,
					World.tilesizey / 2f, MathHelper.getNumberFromTime(2) * 360, true);
			return;
		}

		if ((isSolid(world, x, y) & SolidFaces.UP) == SolidFaces.UP) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 0, true);
		}
		if ((isSolid(world, x, y) & SolidFaces.DOWN) == SolidFaces.DOWN) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 180, true);
		}
		if ((isSolid(world, x, y) & SolidFaces.LEFT) == SolidFaces.LEFT) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 270, true);
		}
		if ((isSolid(world, x, y) & SolidFaces.RIGHT) == SolidFaces.RIGHT) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 90, true);
		}
	}

}
