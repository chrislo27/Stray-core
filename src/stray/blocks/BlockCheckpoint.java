package stray.blocks;

import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.util.DamageSource;
import stray.util.MathHelper;
import stray.world.World;

public class BlockCheckpoint extends Block {

	public BlockCheckpoint(String path) {
		super(path);
	}
	
	@Override
	public int getRenderLevel(){
		return 1;
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if(Block.entityIntersects(world, x, y, world.getPlayer())){
			world.setBlock(Blocks.instance().getBlock("checkpointclaimed"), x, y);
			world.setCheckpoint(x, y);
		}
	}

}
