package stray.blocks;

import stray.LevelEditor;
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
		levelEditorGroup = LevelEditor.EditorGroup.TOGGLE;
	}

	String switchColour = "";
	Color renderColour = Color.WHITE;
	
	@Override
	public boolean isSolid(World world, int x, int y) {
		return !world.global.getString(switchColour).equals("on");
	}
	
	@Override
	public void renderWithOffset(World world, int x, int y, float offx, float offy){
		world.batch.setColor(renderColour.r, renderColour.g, renderColour.b, getAlpha(world, x, y));
		world.batch.draw(
				world.main.textures.get("toggle"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		world.batch.setColor(1, 1, 1, world.batch.getColor().a);
		world.batch.draw(
				world.main.textures.get("toggle_warning"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		world.batch.setColor(1, 1, 1, 1);
	}

}
