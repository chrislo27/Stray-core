package stray.entity;

import stray.Main;
import stray.blocks.Block.BlockFaces;
import stray.blocks.BlockEmpty;
import stray.blocks.BlockPlatform;
import stray.util.Coordinate;
import stray.util.EntityMover;
import stray.util.MathHelper;
import stray.util.Sizeable;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

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
	 * indicates that entities that hit this other entity will shift
	 * velocity*forceTransfer to this entity (and stop)
	 */
	public float forceTransfer = 0f;

	public float dragCoefficient = 1;
	public float gravityCoefficient = 1;
	public float bounceCoefficient = 0;

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

	protected void renderTextureCentered(Texture tex, float width, float height) {
		float rx = ((width + (sizex / 2f)) * World.tilesizex) - world.camera.camerax;
		float ry = ((height + (sizey / 2f)) * World.tilesizey) - world.camera.cameray;
		rx -= width / 2f;
		ry -= height / 2f;
		world.batch.draw(tex, rx, Main.convertY(ry + (sizey * World.tilesizey)));
	}

	protected void renderTextureCentered(Texture tex) {
		renderTextureCentered(tex, tex.getWidth(), tex.getHeight());
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

		float drag = world.drag * getLowestDrag() * dragCoefficient;
		if (velox > 0) {
			velox -= drag / Main.TICKS;
			if (velox < 0) velox = 0;
		} else if (velox < 0) {
			velox += drag / Main.TICKS;
			if (velox > 0) velox = 0;
		}

		if (getBlockCollidingDown() == null && getEntityCollidingDown() == null) {
			veloy += (world.gravity / Main.TICKS) * gravityCoefficient;
		}

		if (veloy != 0) {
			int velo = (int) (veloy / Main.TICKS * World.tilesizey);

			if (velo > 0) {
				Coordinate c = getBlockCollidingDown();
				Entity en = getEntityCollidingDown();
				if (c != null) {
					veloy = -veloy * bounceCoefficient;
					velo = 0;
					onCollideDown();
					world.getBlock(c.getX(), c.getY()).onCollideUpFace(world, c.getX(), c.getY(),
							this);
				} else if (en != null) {
					onCollideDown();
					onCollideEntityDown(en);
					velo = 0;
					float delta = Math.abs(this.veloy - en.veloy);
					en.veloy += delta * en.forceTransfer;
					this.veloy -= delta * en.forceTransfer;
					veloy = -veloy * bounceCoefficient;
				}
			} else if (velo < 0) {
				Coordinate c = getBlockCollidingUp();
				Entity en = getEntityCollidingUp();
				if (c != null) {
					veloy = -veloy * bounceCoefficient;
					velo = 0;
					onCollideUp();
					world.getBlock(c.getX(), c.getY()).onCollideDownFace(world, c.getX(), c.getY(),
							this);
				} else if (en != null) {
					onCollideUp();
					onCollideEntityUp(en);
					velo = 0;
					float delta = Math.abs(this.veloy - en.veloy);
					en.veloy += delta * en.forceTransfer;
					this.veloy -= delta * en.forceTransfer;
					veloy = -veloy * bounceCoefficient;
				}
			}
			for (int i = 0; i < Math.abs(velo); i++) {
				if (velo > 0) {
					y += World.tileparty;
					Coordinate c = getBlockCollidingDown();
					Entity en = getEntityCollidingDown();
					if (c != null) {
						veloy = -veloy * bounceCoefficient;
						onCollideDown();
						world.getBlock(c.getX(), c.getY()).onCollideUpFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideDown();
						onCollideEntityDown(en);
						float delta = Math.abs(this.veloy - en.veloy);
						en.veloy += delta * en.forceTransfer;
						this.veloy -= delta * en.forceTransfer;
						veloy = -veloy * bounceCoefficient;
						break;
					}
				} else if (velo < 0) {
					y -= World.tileparty;
					Coordinate c = getBlockCollidingUp();
					Entity en = getEntityCollidingUp();
					if (c != null) {
						veloy = -veloy * bounceCoefficient;
						onCollideUp();
						world.getBlock(c.getX(), c.getY()).onCollideDownFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideUp();
						onCollideEntityUp(en);
						float delta = Math.abs(this.veloy - en.veloy);
						en.veloy += delta * en.forceTransfer;
						this.veloy -= delta * en.forceTransfer;
						veloy = -veloy * bounceCoefficient;
						break;
					}
				}
			}
		}

		if (velox != 0) {

			int velo = (int) (velox / Main.TICKS * World.tilesizex);

			if (velo > 0) {
				Coordinate c = getBlockCollidingRight();
				Entity en = getEntityCollidingRight();
				if (c != null) {
					velox = -velox * bounceCoefficient;
					velo = 0;
					onCollideRight();
					world.getBlock(c.getX(), c.getY()).onCollideLeftFace(world, c.getX(), c.getY(),
							this);
				} else if (en != null) {
					onCollideRight();
					onCollideEntityRight(en);
					velo = 0;
					float delta = this.velox - en.velox;
					en.velox += delta * en.forceTransfer;
					this.velox -= delta * en.forceTransfer;
					velox = -velox * bounceCoefficient;
				}
			} else if (velo < 0) {
				Coordinate c = getBlockCollidingLeft();
				Entity en = getEntityCollidingLeft();
				if (c != null) {
					velox = -velox * bounceCoefficient;
					velo = 0;
					onCollideLeft();
					world.getBlock(c.getX(), c.getY()).onCollideRightFace(world, c.getX(),
							c.getY(), this);
				} else if (en != null) {
					onCollideLeft();
					onCollideEntityLeft(en);
					velo = 0;
					float delta = this.velox - en.velox;
					en.velox += delta * en.forceTransfer;
					this.velox -= delta * en.forceTransfer;
					velox = -velox * bounceCoefficient;
				}
			}
			for (int i = 0; i < Math.abs(velo); i++) {
				if (velo > 0) {
					x += World.tilepartx;
					Coordinate c = getBlockCollidingRight();
					Entity en = getEntityCollidingRight();
					if (c != null) {
						velox = -velox * bounceCoefficient;
						onCollideRight();
						world.getBlock(c.getX(), c.getY()).onCollideLeftFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideRight();
						onCollideEntityRight(en);
						float delta = this.velox - en.velox;
						en.velox += delta * en.forceTransfer;
						this.velox -= delta * en.forceTransfer;
						velox = -velox * bounceCoefficient;
						break;
					}
				} else if (velo < 0) {
					x -= World.tilepartx;
					Coordinate c = getBlockCollidingLeft();
					Entity en = getEntityCollidingLeft();
					if (c != null) {
						velox = -velox * bounceCoefficient;
						onCollideLeft();
						world.getBlock(c.getX(), c.getY()).onCollideRightFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideLeft();
						onCollideEntityLeft(en);
						float delta = this.velox - en.velox;
						en.velox += delta * en.forceTransfer;
						this.velox -= delta * en.forceTransfer;
						velox = -velox * bounceCoefficient;
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

	public void onCollideDown() {

	}

	public void onCollideEntityLeft(Entity e) {

	}

	public void onCollideEntityRight(Entity e) {

	}

	public void onCollideEntityUp(Entity e) {

	}

	public void onCollideEntityDown(Entity e) {

	}

	public boolean isDead() {
		return false;
	}

	public Coordinate collidingAnyBlock() {
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

	public Entity getEntityCollidingUp() {
		for (Entity e : world.entities) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (x * World.tilesizex + sizex * World.tilesizex)) > ((int) (e.x * World.tilesizex))
						&& ((int) (x * World.tilesizex)) < ((int) (e.x * World.tilesizex + e.sizex
								* World.tilesizex))) {
					if (((int) (this.y * World.tilesizey)) == ((int) (e.y * World.tilesizey + e.sizey
							* World.tilesizey))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Entity getEntityCollidingDown() {
		for (Entity e : world.entities) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (x * World.tilesizex + sizex * World.tilesizex)) > ((int) (e.x * World.tilesizex))
						&& ((int) (x * World.tilesizex)) < ((int) (e.x * World.tilesizex + e.sizex
								* World.tilesizex))) {
					if (((int) (e.y * World.tilesizey)) == ((int) (this.y * World.tilesizey + this.sizey
							* World.tilesizey))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Entity getEntityCollidingLeft() {

		for (Entity e : world.entities) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (y * World.tilesizey + sizey * World.tilesizey)) > ((int) (e.y * World.tilesizey))
						&& ((int) (y * World.tilesizey)) < ((int) (e.y * World.tilesizey + e.sizey
								* World.tilesizey))) {
					if (((int) (this.x * World.tilesizex)) == ((int) (e.x * World.tilesizex + e.sizex
							* World.tilesizex))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Entity getEntityCollidingRight() {

		for (Entity e : world.entities) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (y * World.tilesizey + sizey * World.tilesizey)) > ((int) (e.y * World.tilesizey))
						&& ((int) (y * World.tilesizey)) < ((int) (e.y * World.tilesizey + e.sizey
								* World.tilesizey))) {
					if (((int) (e.x * World.tilesizex)) == ((int) (this.x * World.tilesizex + this.sizex
							* World.tilesizex))) {
						return e;
					}
				}
			}
		}

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
			if ((world.getBlock((posx + i) / World.tilesizex, (posy / World.tilesizey) - 1)
					.isSolid(world, (posx + i) / World.tilesizex, (posy / World.tilesizey) - 1) & BlockFaces.DOWN) == BlockFaces.DOWN) {
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
			if ((world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) & BlockFaces.UP) == BlockFaces.UP) {
				return Coordinate.global.setPosition((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey));
			}
		}

		return null;
	}

	public float getLowestDrag() {
		if (getBlockCollidingDown() == null) return BlockEmpty.DRAG;

		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return 1;
		if ((y + sizey) >= world.sizey) return 1;

		float lowest = 1;

		for (int i = 0; i < boundx; i++) {
			if ((world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) & BlockFaces.UP) == BlockFaces.UP) {
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
		if (getBlockCollidingDown() == null) return BlockEmpty.DRAG;

		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return 1;
		if ((y + sizey) >= world.sizey) return 1;

		float highest = Float.MIN_NORMAL;

		for (int i = 0; i < boundx; i++) {
			if ((world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) & BlockFaces.UP) == BlockFaces.UP) {
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
			if ((world.getBlock(((posx) / World.tilesizex) - 1, ((posy + i) / World.tilesizey))
					.isSolid(world, ((posx) / World.tilesizex) - 1, ((posy + i) / World.tilesizey)) & BlockFaces.RIGHT) == BlockFaces.RIGHT) {
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
			if ((world
					.getBlock(((posx + boundx) / World.tilesizex), ((posy + i) / World.tilesizey))
					.isSolid(world, ((posx + boundx) / World.tilesizex),
							((posy + i) / World.tilesizey)) & BlockFaces.LEFT) == BlockFaces.LEFT) {
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
				if (e == this) continue;
				return e;
			}
		}

		return null;
	}

	public Color getColor() {
		if (tint == null) {
			tint = new Color(Main.getRandom().nextFloat(), Main.getRandom().nextFloat(), Main
					.getRandom().nextFloat(), 1);
		}
		return tint;
	}

	public void accelerate(float x, float y, boolean limitSpeed) {
		if (x > 0) {
			velox += (x + (world.drag * Gdx.graphics.getDeltaTime()))
					* Math.max(World.tilepartx, Math.abs(getHighestDrag()));
			if (limitSpeed) if (velox > maxspeed) velox = maxspeed;
		} else if (x < 0) {
			velox += (x - (world.drag * Gdx.graphics.getDeltaTime()))
					* Math.max(World.tilepartx, Math.abs(getHighestDrag()));
			if (limitSpeed) if (velox < -maxspeed) velox = -maxspeed;
		}
		if (y > 0) {
			veloy += y + (world.drag * Gdx.graphics.getDeltaTime());
			// if (dragcalc) if (veloy > maxspeed) veloy = maxspeed;
		} else if (y < 0) {
			veloy += y - (world.drag * Gdx.graphics.getDeltaTime());
			// if (dragcalc) if (veloy < -maxspeed) veloy = -maxspeed;
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
