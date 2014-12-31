package stray.world;

import com.badlogic.gdx.utils.Pool.Poolable;

import stray.blocks.Block;
import stray.blocks.Blocks;


public class BlockUpdate implements Poolable{
	
	protected int x;
	protected int y = 0;
	
	protected Block block = Blocks.defaultBlock();
	protected String meta = null;
	
	public BlockUpdate(){
		
	}
	
	public BlockUpdate init(int x, int y, Block b, String m){
		this.x = x;
		this.y = y;
		block = b;
		meta = m;
		return this;
	}
	

	@Override
	public void reset() {
		meta = null;
		block = Blocks.defaultBlock();
		x = y = 0;
	}
}
