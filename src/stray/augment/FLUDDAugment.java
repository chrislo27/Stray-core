package stray.augment;

import stray.Main;
import stray.entity.EntityPlayer;
import stray.util.MathHelper;
import stray.util.ParticlePool;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class FLUDDAugment extends Augment {

	public static final float PARTICLE_SPEED = 10f;

	private long lastUse = System.currentTimeMillis();

	private float lastGravCoeff = 1f;

	private boolean touchedDown = false;

	@Override
	public void onActivateStart(World world) {
		lastUse = System.currentTimeMillis();
		world.getPlayer().veloy = 0;
		lastGravCoeff = world.getPlayer().gravityCoefficient;
		world.getPlayer().gravityCoefficient = 0;
		touchedDown = false;
	}

	@Override
	public void onActivate(World world) {
		EntityPlayer player = world.getPlayer();

		if (player.gravityCoefficient > 0) return;

		for (int i = 0; i < 1; i++) {
			createParticle(world, player.x + (player.sizex / 4f), player.y + (player.sizey - 0.2f)
					+ (i * 8), "magnetglow");
			createParticle(world, player.x + ((player.sizex / 4f) * 3), player.y
					+ (player.sizey - 0.2f) + (i * 8), "magnetglow");
		}

		if (player.health < player.maxhealth) {
			player.health += (Gdx.graphics.getRawDeltaTime() / 32f);
			player.health = MathUtils.clamp(player.health, 0f, player.maxhealth);
		}
		if (player.fireTime > 0) player.fireTime = 0;

		world.camera.shake(Gdx.graphics.getDeltaTime(), 0.1f, false);
	}

	private void createParticle(World world, float x, float y, String type) {
		world.particles.add(ParticlePool.obtain().setPosition(x, y).setVelocity(0, PARTICLE_SPEED)
				.setTint(getColor()).setTexture(type).setLifetime(15).setDestroyOnBlock(true)
				.setAlpha(MathHelper.clampNumberFromTime(1 / 3f) + 0.25f));
	}

	@Override
	public void onActivateEnd(World world) {
		world.getPlayer().gravityCoefficient = lastGravCoeff;
		if (touchedDown == false) {
			if (world.getPlayer().getBlockCollidingDown() != null
					|| world.getPlayer().getEntityCollidingDown() != null) touchedDown = true;
		}
	}

	@Override
	public void onActivateTick(World world) {
		world.getPlayer().veloy /= 2;
	}

	@Override
	public void renderUi(Main main, World world) {
		if (touchedDown == false) {
			if (world.getPlayer().getBlockCollidingDown() != null
					|| world.getPlayer().getEntityCollidingDown() != null) touchedDown = true;
		}
	}

	@Override
	public String getName() {
		return "augment.name.fludd";
	}

	@Override
	public Color getColor() {
		return Augment.reused.set(20 / 255f, 153 / 255f, 219 / 255f, 1);
	}

	@Override
	public long getUseTime() {
		return 1500;
	}

	@Override
	public boolean canUse(World world) {
		return (world.getPlayer().getBlockCollidingDown() == null) && (touchedDown);
	}

	@Override
	public boolean isInUse(World world) {
		return world.getPlayer().gravityCoefficient <= 0;
	}

}
