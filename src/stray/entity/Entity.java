package stray.entity;

import stray.Main;
import stray.blocks.BlockPlatform;
import stray.util.Coordinate;
import stray.util.EntityMover;
import stray.util.MathHelper;
import stray.util.Sizeable;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public abstract class Entity implements EntityMover, Sizeable {

	public transient World world;
	public float x = 0;
	public float y = 0;
	public float sizex = 1;
	public float sizey = 1;

	/**
	 * collides with blocks
	 */
	public boolean hasBlockCollision = true;
	/**
	 * collides with other entities with this flag as true
	 */
	public boolean hasEntityCollision = false;
	/**
	 * if it can collide with other members of the same class
	 */
	public boolean canCollideItself = true;
	public Array<Class<? extends Entity>> ignoreCollision = new Array();

	/**
	 * no drag applied
	 */
	public boolean nodrag = false;
	
	public float gravityCoefficient = 1;

	public float velox = 0; // speed blocks/sec
	public float veloy = 0; // speed blocks/sec
	public float accspeed = 1.5f; // acceleration blocks/sec
	public float maxspeed = 1.5f; // speed cap blocks/sec

	private Color tint = getColor();

	public Entity(World w, float posx, float posy) {
		world = w;
		x = posx;
		y = posy;
		prepare();
	}

	/**
	 * called on creation
	 */
	public abstract void prepare();

	public void render(float delta) {

		renderSelf((x * World.tilesizex) - world.camera.camerax,
				Main.convertY((y * World.tilesizey) - world.camera.cameray));

	}

	/**
	 * coordinates are the top-left corner of x, y
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void renderSelf(float x, float y);

	protected void renderTextureCentred(Texture tex, float width, float height) {
		float rx = ((width + (sizex / 2f)) * World.tilesizex) - world.camera.camerax;
		float ry = ((height + (sizey / 2f)) * World.tilesizey) - world.camera.cameray;
		rx -= width / 2f;
		ry -= height / 2f;
		world.batch.draw(tex, rx, Main.convertY(ry + (sizey * World.tilesizey)));
	}

	protected void renderTextureCentred(Texture tex) {
		renderTextureCentred(tex, tex.getWidth(), tex.getHeight());
	}

	/**
	 * called every render update BEFORE rendering
	 */
	public void renderUpdate() {

	}

	/**
	 * 
	 * @return true to stop death
	 */
	public boolean onDeath() {
		return false;
	}

	/**
	 * called every tick, before rendering
	 */
	public void tickUpdate() {

		if (!nodrag) {
			float drag = world.drag * getLowestDrag();
			if (velox > 0) {
				velox -= drag / Main.TICKS;
				if (velox < 0) velox = 0;
			} else if (velox < 0) {
				velox += drag / Main.TICKS;
				if (velox > 0) velox = 0;
			}
		}
			if (getBlockCollidingDown() == null) {
				veloy += (world.gravity / Main.TICKS) * gravityCoefficient;
			}
		

		if (veloy != 0) {
			int velo = (int) (veloy / Main.TICKS * World.tilesizey);
			if (velo > 0) {
				if (getBlockCollidingDown() != null) {
					veloy = 0;
					velo = 0;
					onCollideDown();
				}
			} else if (velo < 0) {
				if (getBlockCollidingUp() != null) {
					veloy = 0;
					velo = 0;
					onCollideUp();
				}
			}
			for (int i = 0; i < Math.abs(velo); i++) {
				if (velo > 0) {
					y += World.tileparty;
					if (getBlockCollidingDown() != null) {
						veloy = 0;
						onCollideDown();
						break;
					}
				} else if (velo < 0) {
					y -= World.tileparty;
					if (getBlockCollidingUp() != null) {
						veloy = 0;
						onCollideUp();
						break;
					}
				}
			}
		}

		if (velox != 0) {

			int velo = (int) (velox / Main.TICKS * World.tilesizex);

			if (velo > 0) {
				if (getBlockCollidingRight() != null) {
					velox = 0;
					velo = 0;
					onCollideRight();
				}
			} else if (velo < 0) {
				if (getBlockCollidingLeft() != null) {
					velox = 0;
					velo = 0;
					onCollideLeft();
				}
			}
			for (int i = 0; i < Math.abs(velo); i++) {
				if (velo > 0) {
					x += World.tilepartx;
					if (getBlockCollidingRight() != null) {
						velox = 0;
						onCollideRight();
						break;
					}
				} else if (velo < 0) {
					x -= World.tilepartx;
					if (getBlockCollidingLeft() != null) {
						velox = 0;
						onCollideLeft();
						break;
					}
				}
			}
		}

	}

	public boolean intersectingOther(Entity other) {
		return (MathHelper.intersects(x, y, sizex, sizey, other.x, other.y, other.sizex,
				other.sizey));
	}

	@Override
	public float getCurrentX() {
		return x;
	}

	@Override
	public float getCurrentY() {
		return y;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getWidth() {
		return sizex;
	}

	@Override
	public float getHeight() {
		return sizey;
	}

	/**
	 * called AFTER velocity has been altered after collision
	 */
	public void onCollideLeft() {

	}

	/**
	 * called AFTER velocity has been altered after collision
	 */
	public void onCollideRight() {

	}

	/**
	 * called AFTER velocity has been altered after collision
	 */
	public void onCollideUp() {

	}

	/**
	 * called AFTER velocity has been altered after collision
	 */
	public void onCollideDown() {

	}

	public boolean isDead() {
		return false;
	}

	public Coordinate collidingAnywhere() {
		Coordinate c = null;

		c = getBlockCollidingUp();
		if (c != null) return c;

		c = getBlockCollidingDown();
		if (c != null) return c;

		c = getBlockCollidingLeft();
		if (c != null) return c;

		c = getBlockCollidingRight();
		if (c != null) return c;

		return null;
	}

	public Coordinate getBlockCollidingUp() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if (posy % World.tilesizey != 0) return null;
		if (y <= 0) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundx; i++) {
			if (world.getBlock((posx + i) / World.tilesizex, (posy / World.tilesizey) - 1).isSolid(
					world, (posx + i) / World.tilesizex, (posy / World.tilesizey) - 1)) {
				return Coordinate.global.setPosition((posx + i) / World.tilesizex,
						(posy / World.tilesizey) - 1);
			}
		}

		return null;
	}

	public Coordinate getBlockCollidingDown() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return null;
		if ((y + sizey) >= world.sizey) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundx; i++) {
			if (world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey))
					|| world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) instanceof BlockPlatform) {
				return Coordinate.global.setPosition((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey));
			}
		}

		return null;
	}

	public float getLowestDrag() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return 1;
		if ((y + sizey) >= world.sizey) return 1;

		float lowest = 1;

		for (int i = 0; i < boundx; i++) {
			if (world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey))
					|| world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) instanceof BlockPlatform) {
				if (world.getBlock((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
						(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey)) < lowest) {
					lowest = world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
							(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey));
				}
			}
		}

		return lowest;
	}

	public float getHighestDrag() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return 1;
		if ((y + sizey) >= world.sizey) return 1;

		float highest = 1;

		for (int i = 0; i < boundx; i++) {
			if (world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey))
					|| world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) instanceof BlockPlatform) {
				if (world.getBlock((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
						(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey)) > highest) {
					highest = world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
							(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey));
				}
			}
		}

		return highest;
	}

	public Coordinate getBlockCollidingLeft() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if (posx % World.tilesizex != 0) return null;
		if (posx <= 0) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundy; i++) {
			if (world.getBlock(((posx) / World.tilesizex) - 1, ((posy + i) / World.tilesizey))
					.isSolid(world, ((posx) / World.tilesizex) - 1, ((posy + i) / World.tilesizey))) {
				return Coordinate.global.setPosition(((posx) / World.tilesizex) - 1,
						((posy + i) / World.tilesizey));
			}
		}

		return null;
	}

	public Coordinate getBlockCollidingRight() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posx + boundx) % World.tilesizex != 0) return null;
		if ((x + sizex) >= (world.sizex)) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundy; i++) {
			if (world.getBlock(((posx + boundx) / World.tilesizex), ((posy + i) / World.tilesizey))
					.isSolid(world, ((posx + boundx) / World.tilesizex),
							((posy + i) / World.tilesizey))) {
				return Coordinate.global.setPosition(((posx + boundx) / World.tilesizex),
						((posy + i) / World.tilesizey));
			}
		}

		return null;
	}

	public Entity entityColliding() {
		if (!hasEntityCollision) return null;
		for (Entity e : world.entities) {
			if (intersectingOther(e)) {
				if (!e.hasEntityCollision) continue;
				if (e.getClass().isInstance(this) && !canCollideItself) continue;
				if (e == this) continue;
				if (ignoreCollision.contains(e.getClass(), true)) continue;
				return e;
			}
		}

		return null;
	}

	public Color getColor() {
		if (tint == null) {
			tint = new Color(Main.getRandom().nextFloat(), Main.getRandom().nextFloat(),
					Main.getRandom().nextFloat(), 1);
		}
		return tint;
	}

	public void accelerate(float x, float y, boolean limitSpeed) {
		if (x > 0) {
			velox += (x + (world.drag * Gdx.graphics.getDeltaTime())) * Math.max(World.tilepartx, Math.abs(getLowestDrag()));
			if (limitSpeed) if (velox > maxspeed) velox = maxspeed;
		} else if (x < 0) {
			velox += (x - (world.drag * Gdx.graphics.getDeltaTime())) * Math.max(World.tilepartx, Math.abs(getLowestDrag()));
			if (limitSpeed) if (velox < -maxspeed) velox = -maxspeed;
		}
		if (y > 0) {
			veloy += y + (world.drag * Gdx.graphics.getDeltaTime());
			//if (dragcalc) if (veloy > maxspeed) veloy = maxspeed;
		} else if (y < 0) {
			veloy += y - (world.drag * Gdx.graphics.getDeltaTime());
			//if (dragcalc) if (veloy < -maxspeed) veloy = -maxspeed;
		}

	}

	public void accelerate(float x, float y) {
		accelerate(x, y, false);
	}

	public void moveUp() {
		if (getBlockCollidingUp() == null && veloy > -maxspeed) {
			accelerate(0, -accspeed * Gdx.graphics.getDeltaTime(), true);
		}
	}

	public void moveDown() {
		if (getBlockCollidingDown() == null && veloy < maxspeed) {
			accelerate(0, accspeed * Gdx.graphics.getDeltaTime(), true);
		}
	}

	public void moveLeft() {
		if (getBlockCollidingLeft() == null && velox > -maxspeed) {
			accelerate(-accspeed * Gdx.graphics.getDeltaTime(), 0, true);
		}
	}

	public void moveRight() {
		if (getBlockCollidingRight() == null && velox < maxspeed) {
			accelerate(accspeed * Gdx.graphics.getDeltaTime(), 0, true);
		}
	}

}
