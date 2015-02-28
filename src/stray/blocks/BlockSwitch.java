package stray.blocks;

import stray.LevelEditor;
import stray.Main;
import stray.entity.Entity;
import stray.entity.types.Weighted;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class BlockSwitch extends Block implements AffectsColour {

	public BlockSwitch(Color c, String colour) {
		super(null);
		this.switchColour = colour;
		renderColour = c;
		levelEditorGroup = LevelEditor.EditorGroup.BUTTON;
	}

	Color renderColour = Color.WHITE;
	String switchColour = "";

	public void tickUpdate(World world, int x, int y) {
		if (isEntityOn(world, x, y)) {
			if (!world.global.getString(switchColour).equals("on")) {
				world.global.setString(switchColour, "on");
				Block.playSound(x, y, world.camera.camerax, world.camera.cameray,
						world.main.manager.get(AssetMap.get("switchsfx"), Sound.class), 1, 1);
			}
			world.setMeta("step", x, y);
			return;
		}

		if (!world.global.getString(switchColour).equals("") && world.getMeta(x, y) != null) {
			world.setMeta(null, x, y);
			
			if(!areOtherBlocksOn(world, x, y, switchColour)){
				world.global.setString(switchColour, "");
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

	public void renderWithOffset(World world, int x, int y, float offx, float offy) {
		world.batch.draw(
				world.main.textures.get("switch"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		world.batch.setColor(renderColour);
		world.batch.draw(
				world.main.textures.get("switch_bg"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		world.batch.setColor(1, 1, 1, 1);
		world.batch.draw(
				world.main.textures.get("toggle_warning"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		
		if (world.getMeta(x, y) != null) if (world.getMeta(x, y).equals("step")) {
			world.batch.setColor(Color.YELLOW);
			world.batch.draw(world.main.manager.get(AssetMap.get("entityshield"), Texture.class), x
					* world.tilesizex - world.camera.camerax + offx,
					Main.convertY(y * world.tilesizey - world.camera.cameray + World.tilesizey + offy));
			world.batch.setColor(1, 1, 1, 1);
		}
	}

	@Override
	public boolean colourOn(World world, int x, int y) {
		return isEntityOn(world, x, y);
	}

	@Override
	public String getColour(World world, int x, int y) {
		return switchColour;
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
