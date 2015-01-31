package stray.blocks;

import stray.Main;
import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.util.MathHelper;
import stray.world.World;


public class BlockFire extends Block{

	public BlockFire(String path) {
		super(path);
	}
	
	float damagePerTick = ((1f / 3f)) / (Main.TICKS * 1f);
	
	@Override
	public void tickUpdate(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (e instanceof EntityLiving) {
				if (MathHelper.intersects(x, y, 1, 1, e.x,
						e.y, e.sizex, e.sizey)) {
					((EntityLiving) e).heal(damagePerTick);
				}
			}
		}
	}

}
