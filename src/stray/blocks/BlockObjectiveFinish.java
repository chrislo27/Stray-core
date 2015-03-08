package stray.blocks;

import stray.Main;
import stray.objective.Objective;
import stray.objective.Objectives;
import stray.world.World;


public class BlockObjectiveFinish extends Block{

	public BlockObjectiveFinish(String path) {
		super(path);
	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}
	
	@Override
	public void tickUpdate(World world, int x, int y) {
		super.tickUpdate(world, x, y);
		
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			world.completeObjective(Objectives.instance().map.get(world.getMeta(x, y)));
			world.setBlock(null, x, y);
		}
	}
	
	@Override
	public void render(World world, int x, int y) {
		if(world.main.getScreen() != Main.LEVELEDITOR) world.batch.setColor(1, 1, 1, 0.1f);
		
		for(Objective o : world.objectives){
			if(world.getMeta(x, y) == Objectives.instance().reverse.get(o.id)){
				world.batch.setColor(1, 1, 1, 1);
				break;
			}
		}
		
		super.renderWithOffset(world, x, y, 0, BlockCollectible.getFloatingOffset(world, x, y));
		world.batch.setColor(1, 1, 1, 1);
	}
}
