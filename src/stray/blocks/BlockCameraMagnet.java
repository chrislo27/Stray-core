package stray.blocks;

import stray.Main;
import stray.world.World;

public class BlockCameraMagnet extends Block {

	public BlockCameraMagnet(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		if (world.main.getScreen() != null) if ((world.main.getScreen() == Main.LEVELEDITOR)
				|| (world.main.getScreen() == Main.TESTLEVEL)) super.render(world, x, y);
	}

}
