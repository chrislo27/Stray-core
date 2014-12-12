package stray.blocks;

import stray.World;
import stray.entity.Entity;


public abstract class BlockSpawner extends Block{

	public BlockSpawner(String path) {
		super(path);
	}
	
	
	@Override
	public void tickUpdate(World world, int x, int y){
		Entity e = getEntity(world, x, y);
		world.entities.add(e);
		
		world.setBlock(null, x, y);
	}
	
	
	public abstract Entity getEntity(World world, int x, int y);
	

}
