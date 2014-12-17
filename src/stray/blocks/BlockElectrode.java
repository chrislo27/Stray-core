package stray.blocks;

import stray.Main;
import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.world.World;


public class BlockElectrode extends Block{

	public BlockElectrode(String path) {
		super(path);
		this.useConTextures();
	}
	
	@Override
	public void tickUpdate(World world, int x, int y){
		for(Entity e : world.entities){
			if(e instanceof EntityLiving){
				if(Block.entityIntersects(world, x, y, e))
				((EntityLiving) e).damage(1);
			}
		}
	}
	
	@Override
	public void render(World world, int x, int y){
		super.render(world, x, y);
		world.batch.setColor(1, 1, 1, world.batch.getColor().a * 0.5f);
		
		drawConnectedTexture(world, x, y,
				world.main.animations.get("electric-all").getCurrentFrame(((x / (1f * y + 127)) / x) * Main.getRandomInst().nextFloat()),
				world.main.animations.get("electric-all").getCurrentFrame(((x / (1f * y + 127)) / x) * Main.getRandomInst().nextFloat()),
				world.main.animations.get("electric-ver").getCurrentFrame(((x / (1f * y + 127)) / x) * Main.getRandomInst().nextFloat()),
				world.main.animations.get("electric-hor").getCurrentFrame(((x / (1f * y + 127)) / x) * Main.getRandomInst().nextFloat()));
		world.batch.setColor(1, 1, 1, 1);
	}
}
