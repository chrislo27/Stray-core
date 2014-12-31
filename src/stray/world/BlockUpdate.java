package stray.world;

import stray.blocks.Block;
import stray.blocks.Blocks;


public class BlockUpdate {
	protected int x;

	protected int y = 0;
	
	protected Block block = Blocks.defaultBlock();
	String meta = null;
	
	public BlockUpdate(int x, int y, Block b, String m){
		this.x = x;
		this.y = y;
		block = b;
		meta = m;
	}
	
	public void tick(World world){
		
	}
}
