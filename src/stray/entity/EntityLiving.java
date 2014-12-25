package stray.entity;

import stray.Main;
import stray.Particle;
import stray.ParticlePool;
import stray.ai.BaseAI;
import stray.util.Direction;
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

	public int health = 1;
	public int maxhealth = 1;
	public int invincibility = 0;
	public final int invulnTime = 3;

	public EntityLiving(World world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public void prepare() {
		ai = getNewAI();
	}

	public abstract BaseAI getNewAI();

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
						Main.getRainbow(Main.getRandomInst().nextFloat()
								* (Main.getRandomInst().nextInt() / Main.random(2, Short.MAX_VALUE))))
				.setAlpha(1 / 3f);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
		if (ai != null) ai.tickUpdate();
		if (invincibility > 0) invincibility--;
	}

	/**
	 * 
	 * @param hp
	 * @return false if cancelled
	 */
	public boolean damage(int hp) {
		if (hp == 0) return false;
		if (health == 0) return false;
		if (hp > 0 && invincibility > 0) return false;
		health = MathUtils.clamp(health - hp, 0, maxhealth);
		if(hp > 0) invincibility = invulnTime * Main.TICKS;
		return true;
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

}
