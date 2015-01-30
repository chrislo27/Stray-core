package stray.blocks;

import stray.Main;
import stray.entity.Entity;
import stray.entity.types.Weighted;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class BlockSwitch extends Block implements AffectsColour {

	public BlockSwitch(String path, String colour) {
		super(path);
		this.colour = colour;
	}

	String colour = "";

	public void tickUpdate(World world, int x, int y) {
		if (isEntityOn(world, x, y)) {
			if (!world.global.getValue(colour).equals("on")) {
				world.global.setValue(colour, "on");
				Block.playSound(x, y, world.camera.camerax, world.camera.cameray,
						world.main.manager.get(AssetMap.get("switchsfx"), Sound.class), 1, 1);
			}
			world.setMeta("step", x, y);
			return;
		}

		if (!world.global.getValue(colour).equals("") && world.getMeta(x, y) != null) {
			world.setMeta(null, x, y);
			
			if(!areOtherBlocksOn(world, x, y, colour)){
				world.global.setValue(colour, "");
				if (Block.isBlockVisible(world.camera.camerax, world.camera.cameray, x, y)) {
					Block.playSound(x, y, world.camera.camerax, world.camera.cameray,
							world.main.manager.get(AssetMap.get("switchsfx"), Sound.class), 1, 0.8f);
				}
			}
		}
	}

	private boolean isEntityOn(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (e instanceof Weighted) {
				if (Block.entityIntersects(world, x, y - 0.1f, e, 1, 0.1f)) {
					return true;
				}
			}
		}
		return false;
	}

	public void render(World world, int x, int y) {
		super.render(world, x, y);
		if (world.getMeta(x, y) != null) if (world.getMeta(x, y).equals("step")) {
			world.batch.setColor(Color.YELLOW);
			world.batch.draw(world.main.manager.get(AssetMap.get("entityshield"), Texture.class), x
					* world.tilesizex - world.camera.camerax,
					Main.convertY(y * world.tilesizey - world.camera.cameray + World.tilesizey),
					World.tilesizex, World.tilesizey);
			world.batch.setColor(1, 1, 1, 1);
		}
	}

	@Override
	public boolean colourOn(World world, int x, int y) {
		return isEntityOn(world, x, y);
	}

	@Override
	public String getColour(World world, int x, int y) {
		return colour;
	}

	public static boolean areOtherBlocksOn(World world, int x, int y, String yourcolour) {
		for (int i = 0; i < world.sizex; i++) {
			for (int j = 0; j < world.sizey; j++) {
				if (i != x && j != y) {
					if (world.getBlock(i, j) instanceof AffectsColour) {
						if (((AffectsColour) world.getBlock(i, j)).colourOn(world, i, j)
								&& ((AffectsColour) world.getBlock(i, j))
										.getColour(world, i, j).equals(yourcolour)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
