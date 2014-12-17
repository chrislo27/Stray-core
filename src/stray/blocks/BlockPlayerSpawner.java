package stray.blocks;

import stray.entity.Entity;
import stray.entity.EntityPlayer;
import stray.world.World;


public class BlockPlayerSpawner extends BlockSpawner{

	public BlockPlayerSpawner(String path) {
		super(path);
	}

	@Override
	public Entity getEntity(World world, int x, int y) {
		return null;
	}
	
	@Override
	public void tickUpdate(World world, int x, int y){
		if(world.getPlayer() == null){
			world.addPlayer();
		}
		world.getPlayer().x = x;
		world.getPlayer().y = y;
		world.setCheckpoint();
		
		world.setBlock(null, x, y);
	}

}
