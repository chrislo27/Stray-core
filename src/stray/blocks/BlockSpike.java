package stray.blocks;

import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.util.DamageSource;
import stray.util.MathHelper;
import stray.world.World;

public class BlockSpike extends Block {

	public BlockSpike(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (e instanceof EntityLiving) {
				if (MathHelper.intersects(x, y - World.tileparty * 2, 1, World.tileparty * 2, e.x,
						e.y, e.sizex, e.sizey)) {
					if (((EntityLiving) e).invincibility == 0) ((EntityLiving) e).damage(9001, DamageSource.spikes);
				}
			}
		}
	}
}
