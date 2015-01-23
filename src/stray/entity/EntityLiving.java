package stray.entity;

import stray.Main;
import stray.ai.BaseAI;
import stray.util.AssetMap;
import stray.util.Direction;
import stray.util.MathHelper;
import stray.util.Particle;
import stray.util.ParticlePool;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * has AI object
 * 
 *
 */
public abstract class EntityLiving extends Entity {

	protected BaseAI ai;
	protected Direction facing = Direction.RIGHT;

	public float health = 1;
	public float maxhealth = 1;
	public int invincibility = 0;
	public final float invulnTime = 1.5f;

	public int stunTime = 0;

	public EntityLiving(World world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public void prepare() {
		ai = getNewAI();
	}

	public abstract BaseAI getNewAI();

	@Override
	public void render(float delta) {
		if (stunTime > 0) {
			Texture star = world.main.manager.get(AssetMap.get("particlestar"), Texture.class);
			
			if (MathHelper.getNumberFromTime(1.5f) >= 0.5f) {
				world.batch.draw(
						star,
						(x * World.tilesizex) - world.camera.camerax
								+ ((sizex / 4f) * World.tilesizex)
								- ((MathHelper.clampHalf(1.5f) - 0.25f) * star.getWidth() * 3f),
						Main.convertY((y * World.tilesizey) - world.camera.cameray + (star.getHeight() / 4f))
								- ((star.getHeight() / 3f) * (MathHelper.clampHalf(1) - 0.25f) * 2));
				super.render(delta);
				world.batch.draw(
						star,
						(x * World.tilesizex) - world.camera.camerax
								+ ((sizex / 4f) * World.tilesizex)
								+ ((MathHelper.clampHalf(1.5f) - 0.25f) * star.getWidth() * 3f),
						Main.convertY((y * World.tilesizey) - world.camera.cameray + (star.getHeight() / 4f))
								+ ((star.getHeight() / 3f) * (MathHelper.clampHalf(1) - 0.25f) * 2));
			} else {
				world.batch.draw(
						star,
						(x * World.tilesizex) - world.camera.camerax
								+ ((sizex / 4f) * World.tilesizex)
								+ ((MathHelper.clampHalf(1.5f) - 0.25f) * star.getWidth() * 3f),
						Main.convertY((y * World.tilesizey) - world.camera.cameray + (star.getHeight() / 4f))
								+ ((star.getHeight() / 3f) * (MathHelper.clampHalf(1) - 0.25f) * 2));
				super.render(delta);
				world.batch.draw(
						star,
						(x * World.tilesizex) - world.camera.camerax
								+ ((sizex / 4f) * World.tilesizex)
								- ((MathHelper.clampHalf(1.5f) - 0.25f) * star.getWidth() * 3f),
						Main.convertY((y * World.tilesizey) - world.camera.cameray + (star.getHeight() / 4f))
								- ((star.getHeight() / 3f) * (MathHelper.clampHalf(1) - 0.25f) * 2));
			}
		}else{
			super.render(delta);
		}
	}

	/**
	 * also handles invuln flashing
	 * 
	 * @param sprite
	 * @param x
	 * @param y
	 */
	public void drawSpriteWithFacing(Texture sprite, float x, float y) {
		world.batch.setColor(1, 1, 1,
				(invincibility > 0 ? (System.currentTimeMillis() / 100 % 2 == 0 ? 0.25f : 0.5f)
						: world.batch.getColor().a));
		world.batch.draw(sprite, x - (sprite.getWidth() - (sizex * World.tilesizex)) / 2, y
				- World.tilesizey + (sprite.getHeight() - (sizey * World.tilesizey)) / 2,
				sprite.getWidth(), sprite.getHeight(), 0, 0, sprite.getWidth(), sprite.getHeight(),
				(facing != Direction.RIGHT), false);
		world.batch.setColor(1, 1, 1, 1);
	}

	public void poof() {
		// centre
		world.particles.add(getBaseParticle());
		world.particles.add(getBaseParticle().setPosition((float) x, (float) y));
		world.particles.add(getBaseParticle().setPosition((float) (x + sizex), (float) y));
		world.particles
				.add(getBaseParticle().setPosition((float) (x + sizex), (float) (y + sizey)));
		world.particles.add(getBaseParticle().setPosition((float) x, (float) (y + sizey)));

		world.particles.add(getBaseParticle().setPosition((float) (x + (sizex / 2)), (float) y));
		world.particles.add(getBaseParticle().setPosition((float) (x + (sizex / 2)),
				(float) (y + (sizey))));
		world.particles.add(getBaseParticle().setPosition((float) (x + (sizex)),
				(float) (y + (sizey / 2))));
		world.particles.add(getBaseParticle().setPosition((float) x, (float) (y + (sizey / 2))));

		// moving particles
		world.particles.add(getBaseParticle().setPosition((float) x, (float) y).setVelocity(-1.5f,
				-1.5f));
		world.particles.add(getBaseParticle().setPosition((float) (x + sizex), (float) y)
				.setVelocity(1.5f, -1.5f));
		world.particles.add(getBaseParticle().setPosition((float) (x + sizex), (float) (y + sizey))
				.setVelocity(1.5f, 1.5f));
		world.particles.add(getBaseParticle().setPosition((float) x, (float) (y + sizey))
				.setVelocity(-1.5f, 1.5f));

		world.particles.add(getBaseParticle().setPosition((float) (x + (sizex / 2)), (float) y)
				.setVelocity(0f, -1.5f));
		world.particles.add(getBaseParticle().setPosition((float) (x + (sizex / 2)),
				(float) (y + (sizey))).setVelocity(0f, 1.5f));
		world.particles.add(getBaseParticle().setPosition((float) (x + (sizex)),
				(float) (y + (sizey / 2))).setVelocity(1.5f, 0f));
		world.particles.add(getBaseParticle().setPosition((float) x, (float) (y + (sizey / 2)))
				.setVelocity(-1.5f, 0f));

	}

	private Particle getBaseParticle() {
		return ParticlePool
				.obtain()
				.setPosition((float) (x + (sizex / 2)), (float) (y + (sizey / 2)))
				.setLifetime(0.4f)
				.setTexture("particlecircle")
				.setTint(
						Main.getRainbow(Main.getRandom().nextFloat()
								* (Main.getRandom().nextInt() / Main.random(2, Short.MAX_VALUE)), 1))
				.setAlpha(1 / 3f);
	}

	@Override
	public void tickUpdate() {
		if (stunTime > 0) {
			stunTime--;
			return;
		}

		super.tickUpdate();
		if (ai != null) ai.tickUpdate();
		if (invincibility > 0) invincibility--;
	}

	/**
	 * 
	 * @param dmg
	 * @return false if cancelled
	 */
	public boolean damage(float dmg) {
		if (dmg <= 0 || health <= 0) return false;
		if (dmg > 0 && invincibility > 0) return false;
		health = MathUtils.clamp(health - dmg, 0f, maxhealth);
		if (dmg > 0) invincibility = Math.round(invulnTime * Main.TICKS);
		return true;
	}

	/**
	 * adds health to entity, use negative for damage without invuln
	 * 
	 * @param amt
	 */
	public void heal(float amt) {
		health = MathUtils.clamp(health + amt, 0f, maxhealth);
		if (health <= 0f) {
			// this is so the damage override methods get triggered
			health = Math.min(0.0001f, maxhealth);
			damage(1);
		}
	}

	@Override
	public void renderUpdate() {
		super.renderUpdate();
		if (ai != null) ai.renderUpdate();
	}

	public float jump = 3f;

	public void jump() {
		if (getBlockCollidingUp() == null && getBlockCollidingDown() != null) {
			veloy = -jump;
		}
	}

	public void stun(float seconds) {
		stunTime = Math.max(Math.round(seconds * Main.TICKS), 0);
	}

	@Override
	public boolean isDead() {
		return health <= 0;
	}

	@Override
	public boolean onDeath() {
		poof();
		return false;
	}

	@Override
	public void moveLeft() {
		super.moveLeft();
		facing = Direction.LEFT;
	}

	@Override
	public void moveRight() {
		super.moveRight();
		facing = Direction.RIGHT;
	}

	@Override
	public void moveUp() {
		super.moveUp();
		facing = Direction.UP;
	}

	@Override
	public void moveDown() {
		super.moveDown();
		facing = Direction.DOWN;
	}

}
