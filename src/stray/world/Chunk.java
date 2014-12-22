package stray.world;

import stray.blocks.Block;
import stray.blocks.Blocks;

/**
 * Represents a 16x16 area
 * 
 * @author chrislo27
 *
 */
public class Chunk {
	
	public static final int chunkSize = 16;
	
	private Block[][] blocks = new Block[chunkSize][chunkSize];
	private String[][] meta = new String[chunkSize][chunkSize];
	
	private int chunkX = 0;
	private int chunkY = 0;
	
	public Chunk(int x, int y){
		chunkX = x;
		chunkY = y;
		
		for(int bx = 0; bx < chunkSize; bx++){
			for(int by = 0; by < chunkSize; by++){
				blocks[bx][by] = Blocks.defaultBlock();
				meta[bx][by] = null;
			}
		}
	}
	
	/**
	 * 
	 * @param x chunk x
	 * @param y chunk y
	 * @return
	 */
	public Block getBlock(int x, int y){
		if(x < 0 || y < 0 || x >= chunkSize || y >= chunkSize) return Blocks.defaultBlock();
		return blocks[x][y];
	}
	
	/**
	 * 
	 * @param x chunk x
	 * @param y chunk y
	 * @return
	 */
	public String getMeta(int x, int y){
		if(x < 0 || y < 0 || x >= chunkSize || y >= chunkSize) return null;
		return meta[x][y];
	}
	
	/**
	 * 
	 * @param x chunk x
	 * @param y chunk y
	 * @return
	 */
	public void setBlock(Block b, int x, int y){
		if(x < 0 || y < 0 || x >= chunkSize || y >= chunkSize) return;
		blocks[x][y] = b;
	}
	
	/**
	 * 
	 * @param x chunk x
	 * @param y chunk y
	 * @return
	 */
	public void setMeta(String m, int x, int y){
		if(x < 0 || y < 0 || x >= chunkSize || y >= chunkSize) return;
		meta[x][y] = m;
	}
	
}
