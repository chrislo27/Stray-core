package stray.blocks;

import stray.World;
import stray.entity.Entity;
import stray.forme.Forme;

public abstract class BlockForme extends Block {

	public BlockForme(String path) {
		super(path);
	}

	public abstract Forme getForme(Entity e);

	private Forme sample = null;
	
	public Class<? extends Forme> getFormeClass(World world) {
		if(sample == null){
			sample = getForme(null);
		}
		
		return sample.getClass();
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (Block.entityIntersects(world, x, y, world.getPlayer(), 0.8, 0.8)) {
			if (!getFormeClass(world).isInstance(world.getPlayer().currentForme)) {
				world.getPlayer().setForme(getForme(world.getPlayer()));
			}
		}
	}
}
