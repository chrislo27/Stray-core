package stray.blocks.fluid;

import stray.Main;
import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class BlockFluid extends Block {

	public BlockFluid(String path) {
		super(path);
	}
	
	@Override
	public void tickUpdate(World world, int x, int y){
		// first in the checklist: go down/up and merge with any possible fluids down
		
		if(!attemptGravity(world, x, y));
		
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @return true if successful
	 */
	protected boolean attemptGravity(World world, int x, int y){
		if(world.getBlock(x, y + getGravityDirection()) == Blocks.instance().getBlock("empty") || world.getBlock(x, y + getGravityDirection()) == null){
			addFluid(world, x, y, getAmountPerFall());
			if(world.getBlock(x, y + getGravityDirection()) == this){
				if((((BlockFluid) world.getBlock(x, y + getGravityDirection())).getFluidLevel(world, x, y + getGravityDirection())) > 8){
					int leftovers = 8 - ((((BlockFluid) world.getBlock(x, y + getGravityDirection()))).getFluidLevel(world, x, y + getGravityDirection()));
					(((BlockFluid) world.getBlock(x, y + getGravityDirection()))).addFluid(world, x, y + getGravityDirection(), getAmountPerFall());
					setFluidLevel(world, x, y, leftovers);
				}else{
					(((BlockFluid) world.getBlock(x, y + getGravityDirection()))).addFluid(world, x, y + getGravityDirection(), getAmountPerFall());
				}
				return true;
			}else{
				world.setBlock(this, x, y + getGravityDirection());
				((BlockFluid) world.getBlock(x, y + getGravityDirection())).setFluidLevel(world, x, y + getGravityDirection(), getAmountPerFall());
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return 1 if it goes down, -1 if it goes up
	 */
	public int getGravityDirection(){
		return 1;
	}
	
	/**
	 * 
	 * @return the number of layers to fall each update
	 */
	public int getAmountPerFall(){
		return 1;
	}
	
	public int getFluidLevel(World world, int x, int y){
		if(world.getMeta(x, y) == null){
			setFluidLevel(world, x, y, 8);
		}
		
		return MathUtils.clamp(Integer.parseInt(world.getMeta(x, y)), 1, 8);
	}
	
	public void setFluidLevel(World world, int x, int y, int level){
		world.setMeta(MathUtils.clamp(level, 1, 8) + "", x, y);
	}
	public void addFluid(World world, int x, int y, int add){
		setFluidLevel(world, x, y, getFluidLevel(world, x, y) + add);
	}

	@Override
	public void render(World world, int x, int y) {
		if (usingMissingTex) {
			world.batch.draw(
					world.main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class),
					x,
					y,
					World.tilesizex,
					World.tilesizey,
					0,
					0,
					world.main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class)
							.getWidth(),
					(int) (world.main.manager.get(AssetMap.get("blockmissingtexture"),
							Texture.class).getHeight() / (getFluidLevel(world, x, y) - 9f)), false, false);
			return;
		}
		if (animationlink != null) {
			world.batch.draw(world.main.animations.get(animationlink).getCurrentFrame(), x
					* world.tilesizex - world.camera.camerax,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey),
					World.tilesizex, (World.tilesizey / (getFluidLevel(world, x, y) - 9f)));
			return;
		}
		if (path == null) return;

		if (!connectedTextures) {
			if (!variants) {
				world.batch
						.draw(world.main.manager.get(sprites.get("defaulttex"), Texture.class),
								x,
								y,
								World.tilesizex,
								World.tilesizey,
								0,
								0,
								world.main.manager.get(sprites.get("defaulttex"), Texture.class)
										.getWidth(),
								(int) (world.main.manager.get(sprites.get("defaulttex"),
										Texture.class).getHeight() / (getFluidLevel(world, x, y) - 9f)), false, false);
			} else {
				world.batch.draw(
						world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class),
						x,
						y,
						World.tilesizex,
						World.tilesizey,
						0,
						0,
						world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class).getWidth(),
						(int) (world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class).getHeight() / (getFluidLevel(world, x, y) - 9f)), false, false);
			}
		} else {
			drawConnectedTexture(world, x, y,
					world.main.manager.get(sprites.get("corner"), Texture.class),
					world.main.manager.get(sprites.get("full"), Texture.class),
					world.main.manager.get(sprites.get("edgever"), Texture.class),
					world.main.manager.get(sprites.get("edgehor"), Texture.class));
		}
	}

}
