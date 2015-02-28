package stray.blocks;

import stray.Main;
import stray.blocks.Block.SolidFaces;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;


public class BlockDirectional extends Block{

	public BlockDirectional(String path, boolean first) {
		super(path);
		findFirstMatch = first;
	}
	
	boolean findFirstMatch = false;

	@Override
	public void renderWithOffset(World world, int x, int y, float offx, float offy) {

		if ((world.getMeta(x, y) & SolidFaces.UP) == SolidFaces.UP || world.getMeta(x, y) <= SolidFaces.NONE) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 0, true);
			if(findFirstMatch) return;
		}
		if ((world.getMeta(x, y) & SolidFaces.DOWN) == SolidFaces.DOWN) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 180, true);
			if(findFirstMatch) return;
		}
		if ((world.getMeta(x, y) & SolidFaces.LEFT) == SolidFaces.LEFT) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 270, true);
			if(findFirstMatch) return;
		}
		if ((world.getMeta(x, y) & SolidFaces.RIGHT) == SolidFaces.RIGHT) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 90, true);
			if(findFirstMatch) return;
		}
	}
	
}
