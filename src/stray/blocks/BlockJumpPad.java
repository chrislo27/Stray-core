package stray.blocks;

import stray.Main;
import stray.entity.Entity;
import stray.world.World;


public class BlockJumpPad extends Block {

	public BlockJumpPad(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for(Entity e : world.entities){
			if(Block.entityIntersects(world, x, y, e, 1, -0.1f)){
				e.veloy -= 15f;
			}
		}
		
	}
	
}
