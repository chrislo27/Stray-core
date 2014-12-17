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
	
	Block[][] blocks = new Block[chunkSize][chunkSize];
	String[][] meta = new String[chunkSize][chunkSize];
	
	int chunkX = 0;
	int chunkY = 0;
	
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
	
}
