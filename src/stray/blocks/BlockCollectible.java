package stray.blocks;

import stray.LevelEditor.EditorGroup;
import stray.world.World;


public class BlockCollectible extends Block{

	public BlockCollectible(String path, String col) {
		super(path);
		collectible = col;
		this.levelEditorGroup = EditorGroup.COLLECTIBLE;
	}
	
	String collectible = "COLLECTIBLE BULEAH";
	
	@Override
	public void tickUpdate(World world, int x, int y) {
		if(world.getMeta(x, y) != null) return;
		if(Block.entityIntersects(world, x, y, world.getPlayer())){
			world.global.setInt(collectible, world.global.getInt(collectible) + 1);
			world.setMeta("" + this.hashCode(), x, y); // this can be !null
		}
	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}
	
	@Override
	public void render(World world, int x, int y){
		if(world.getMeta(x, y) != null) return;
		super.render(world, x, y);
	}

}