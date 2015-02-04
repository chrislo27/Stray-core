package stray.blocks;

import stray.Main;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class BlockToggle extends BlockFadeable {

	public BlockToggle(Color c, String switc) {
		super(null);
		renderColour = c;
		switchColour = switc;
	}

	String switchColour = "";
	Color renderColour = Color.WHITE;
	
	@Override
	public boolean isSolid(World world, int x, int y) {
		return !world.global.getString(switchColour).equals("on");
	}
	
	@Override
	public void render(World world, int x, int y){
		world.batch.setColor(renderColour.r, renderColour.g, renderColour.b, getAlpha(world, x, y));
		world.batch.draw(
				world.main.textures.get("toggle"), x
						* world.tilesizex - world.camera.camerax,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey),
				World.tilesizex, World.tilesizey);
		world.batch.setColor(1, 1, 1, world.batch.getColor().a);
		world.batch.draw(
				world.main.textures.get("toggle_warning"), x
						* world.tilesizex - world.camera.camerax,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey),
				World.tilesizex, World.tilesizey);
		world.batch.setColor(1, 1, 1, 1);
	}

}
