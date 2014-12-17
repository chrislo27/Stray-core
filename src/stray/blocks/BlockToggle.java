package stray.blocks;

import stray.world.World;

public class BlockToggle extends BlockFadeable {

	public BlockToggle(String path, String switc) {
		super(path);
		switchColour = switc;
	}

	String switchColour = "";

	@Override
	public boolean isSolid(World world, int x, int y) {
		return !world.global.getValue(switchColour).equals("on");
	}

}
