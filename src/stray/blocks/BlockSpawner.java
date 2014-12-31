package stray.blocks;

import stray.Main;
import stray.entity.Entity;
import stray.world.World;


public abstract class BlockSpawner extends Block{

	public BlockSpawner(String path) {
		super(path);
	}
	
	
	@Override
	public void tickUpdate(World world, int x, int y){
		if(world.getMeta(x, y) != null) return;
		Entity e = getEntity(world, x, y);
		world.entities.add(e);
		
		world.setMeta("spawned", x, y);
	}
	
	@Override
	public void render(World world, int x, int y){
		if (world.main.getScreen() != null) if ((world.main.getScreen() == Main.LEVELEDITOR)) super.render(world, x, y);
	}
	
	
	public abstract Entity getEntity(World world, int x, int y);
	

}
