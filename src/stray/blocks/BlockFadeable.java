package stray.blocks;

import stray.World;

public class BlockFadeable extends Block {

	public BlockFadeable(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		if (!isSolid(world, x, y)) {
			world.main.batch.setColor(1, 1, 1, 0.075f);
		} else {
			if (Block.entityIntersects(world, x + World.tilepartx, y + World.tileparty,
					world.getPlayer(), 1f - (World.tilepartx * 2), 1f - (World.tileparty * 2))) {
				world.main.batch.setColor(1, 1, 1, 0.5f);
			}
		}
		super.render(world, x, y);

		world.main.batch.setColor(1, 1, 1, 1);
	}

}
