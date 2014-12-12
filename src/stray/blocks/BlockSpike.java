package stray.blocks;

import stray.World;
import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.util.MathHelper;


public class BlockSpike extends Block {

	public BlockSpike(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for(Entity e : world.entities){
			if(e instanceof EntityLiving){
				if(MathHelper.intersects(x, y - World.tileparty * 2, 1, World.tileparty * 2, e.x, e.y, e.sizex, e.sizey)){
					((EntityLiving) e).damage(9001);
				}
			}
		}
	}
}
