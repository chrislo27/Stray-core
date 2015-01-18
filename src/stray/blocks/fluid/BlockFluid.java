package stray.blocks.fluid;

import stray.Main;
import stray.Settings;
import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.util.AssetMap;
import stray.util.MathHelper;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class BlockFluid extends Block {

	public BlockFluid(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		// first in the checklist: go down/up and merge with any possible fluids
		// down

		if (attemptGravity(world, x, y) == false) {
			if (attemptSpread(world, x, y) == false) {

			}
		}

	}

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @return true if successful
	 */
	protected boolean attemptGravity(World world, int x, int y) {
		return flowTo(world, x, y, x, y + getGravityDirection(), getAmountPerFall());
	}
	
	public static boolean movementDirection(){
		return MathHelper.getNthDigit(System.currentTimeMillis(), 3) < 5;
	}

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @return false if the fluid was trapped on both sides
	 */
	protected boolean attemptSpread(World world, int x, int y) {
		if (!canFlowTo(world, x - 1, y) && !canFlowTo(world, x + 1, y)) return false;

		int left = getFluidLevel(world, x - 1, y);
		int right = getFluidLevel(world, x + 1, y);

		if (movementDirection()) {
			if (left < right && (canFlowTo(world, x - 1, y)) && left < getFluidLevel(world, x, y)) {
				return flowTo(world, x, y, x - 1, y, getFluidViscosity());
			} else if (right < left && (canFlowTo(world, x + 1, y))
					&& right < getFluidLevel(world, x, y)) {
				return flowTo(world, x, y, x + 1, y, getFluidViscosity());
			} else {
				if (movementDirection()) {
					if (left <= getFluidLevel(world, x, y) && (canFlowTo(world, x - 1, y))) {
						return flowTo(world, x, y, x - 1, y, getFluidViscosity());
					}else if(movementDirection()) return false;
					if (right <= getFluidLevel(world, x, y) && (canFlowTo(world, x + 1, y))) {
						return flowTo(world, x, y, x + 1, y, getFluidViscosity());
					}
					return false;
				} else {
					if (right <= getFluidLevel(world, x, y) && (canFlowTo(world, x + 1, y))) {
						return flowTo(world, x, y, x + 1, y, getFluidViscosity());
					}else if(movementDirection()) return false;
					if (left <= getFluidLevel(world, x, y) && (canFlowTo(world, x - 1, y))) {
						return flowTo(world, x, y, x - 1, y, getFluidViscosity());
					}
					return false;
				}
			}
		} else {
			if (right < left && (canFlowTo(world, x + 1, y)) && right < getFluidLevel(world, x, y)) {
				return flowTo(world, x, y, x + 1, y, getFluidViscosity());
			} else if (left < right && (canFlowTo(world, x - 1, y))
					&& left < getFluidLevel(world, x, y)) {
				return flowTo(world, x, y, x - 1, y, getFluidViscosity());
			} else {
				if (movementDirection()) {
					if (left <= getFluidLevel(world, x, y) && (canFlowTo(world, x - 1, y))) {
						return flowTo(world, x, y, x - 1, y, getFluidViscosity());
					}else if(movementDirection()) return false;
					if (right <= getFluidLevel(world, x, y) && (canFlowTo(world, x + 1, y))) {
						return flowTo(world, x, y, x + 1, y, getFluidViscosity());
					}
					return false;
				} else {
					if (right <= getFluidLevel(world, x, y) && (canFlowTo(world, x + 1, y))) {
						return flowTo(world, x, y, x + 1, y, getFluidViscosity());
					}else if(movementDirection()) return false;
					if (left <= getFluidLevel(world, x, y) && (canFlowTo(world, x - 1, y))) {
						return flowTo(world, x, y, x - 1, y, getFluidViscosity());
					}
					return false;
				}
			}
		}
	}

	protected int getFluidViscosity() {
		return 1;
	}

	protected boolean canFlowTo(World world, int x, int y) {
		if (x < 0 || y < 0 || x >= world.sizex || y >= world.sizey) return false;

		if (world.getBlock(x, y) == this) {
			if (getFluidLevel(world, x, y) < 8) return true;
		}
		return (world.getBlock(x, y) == Blocks.instance().getBlock("empty") || world.getBlock(x, y) == null);
	}

	protected boolean flowTo(World world, int startx, int starty, int tox, int toy, int amount) {
		if (amount <= 0) return false;
		amount = MathUtils.clamp(amount, 1, getFluidLevel(world, startx, starty));

		if (canFlowTo(world, tox, toy)) {
			if (world.getBlock(tox, toy) == this) {
				if ((getFluidLevel(world, tox, toy) + amount) > 8) {
					int actualAmount = (8 - (getFluidLevel(world, tox, toy)));
					addFluid(world, startx, starty, -actualAmount);
					world.scheduledUpdates.add(world.buPool.obtain().init(tox, toy, this,
							"" + (getFluidLevel(world, tox, toy) + actualAmount)));
				} else {
					addFluid(world, startx, starty, -amount);
					world.scheduledUpdates.add(world.buPool.obtain().init(tox, toy, this,
							"" + (getFluidLevel(world, tox, toy) + amount)));
				}
				return true;
			} else {
				if (world.getBlock(tox, toy) == Blocks.instance().getBlock("empty")
						|| world.getBlock(tox, toy) == null) {
					addFluid(world, startx, starty, -amount);
					world.scheduledUpdates.add(world.buPool.obtain().init(tox, toy, this,
							"" + amount));
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 
	 * @return 1 if it goes down, -1 if it goes up
	 */
	public int getGravityDirection() {
		return 1;
	}

	/**
	 * 
	 * @return the number of layers to fall each update
	 */
	public int getAmountPerFall() {
		return 8;
	}

	public int getFluidLevel(World world, int x, int y) {
		if (world.getBlock(x, y) != this) return 0;
		if (world.getMeta(x, y) == null) {
			world.setMeta("8", x, y);
			return 8;
		}

		return MathUtils.clamp(Integer.parseInt(world.getMeta(x, y)), 1, 8);
	}

	public void setFluidLevel(World world, int x, int y, int level) {
		if (level <= 0) {
			world.setMeta(null, x, y);
			world.setBlock(null, x, y);
			return;
		}

		world.setMeta(MathUtils.clamp(level, 1, 8) + "", x, y);
	}

	public void addFluid(World world, int x, int y, int add) {
		setFluidLevel(world, x, y, getFluidLevel(world, x, y) + add);
	}

	private float getRenderingCoefficient(World world, int x, int y) {
		if (world.getBlock(x, y - getGravityDirection()) == this) return 1;

		return (getFluidLevel(world, x, y) / 8f);
	}

	@Override
	public void render(World world, int x, int y) {
		if (usingMissingTex) {
			world.batch
					.draw(world.main.manager
							.get(AssetMap.get("blockmissingtexture"), Texture.class),
							x * World.tilesizex - world.camera.camerax,
							Main.convertY(y * World.tilesizey - world.camera.cameray)
									- World.tilesizey, World.tilesizex,
							(int) (World.tilesizey * getRenderingCoefficient(world, x, y)));
			if (Settings.debug) {
				world.main.drawCentered(world.getMeta(x, y) + "", x * World.tilesizex
						- world.camera.camerax + 32,
						Main.convertY(y * World.tilesizey - world.camera.cameray) - World.tilesizey
								+ 15);
			}
			return;
		}
		if (animationlink != null) {
			world.batch
					.draw(world.main.animations.get(animationlink).getCurrentFrame(), x
							* World.tilesizex - world.camera.camerax,
							Main.convertY(y * World.tilesizey - world.camera.cameray)
									- World.tilesizey, World.tilesizex,
							(int) (World.tilesizey * getRenderingCoefficient(world, x, y)));
			return;
		}
		if (path == null) return;

		if (!connectedTextures) {
			if (!variants) {
				world.batch
						.draw(world.main.manager.get(sprites.get("defaulttex"), Texture.class), x
								* World.tilesizex - world.camera.camerax,
								Main.convertY(y * World.tilesizey - world.camera.cameray)
										- World.tilesizey, World.tilesizex,
								(int) (World.tilesizey * getRenderingCoefficient(world, x, y)));
			} else {
				world.batch
						.draw(world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class), x * World.tilesizex - world.camera.camerax,
								Main.convertY(y * World.tilesizey - world.camera.cameray)
										- World.tilesizey, World.tilesizex,
								(int) (World.tilesizey * getRenderingCoefficient(world, x, y)));

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
