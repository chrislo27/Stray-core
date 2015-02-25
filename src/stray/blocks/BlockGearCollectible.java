package stray.blocks;

import stray.Main;
import stray.util.MathHelper;
import stray.world.World;


public class BlockGearCollectible extends BlockCollectible{

	public BlockGearCollectible(String path) {
		super(path, "gears");
	}
	
	@Override
	public void render(World world, int x, int y){
		if(world.getMeta(x, y) != null){
			return;
		}
		
		//super.render(world, x, y);
		
		super.renderWithOffset(world, x, y, 0, (World.tilesizey / 2f) * (MathHelper.clampNumberFromTime(2.5f) - 0.5f));
	}

}
