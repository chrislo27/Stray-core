package stray.augment;

import stray.Main;
import stray.entity.EntityPlayer;
import stray.util.ParticlePool;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class FLUDDAugment extends Augment {

	public static final float PARTICLE_SPEED = 16f;

	private long lastUse = System.currentTimeMillis();
	
	private float lastGravCoeff = 1f;

	@Override
	public void onActivateStart(World world) {
		lastUse = System.currentTimeMillis();
		world.getPlayer().veloy = 0;
		lastGravCoeff = world.getPlayer().gravityCoefficient;
		world.getPlayer().gravityCoefficient = World.tileparty;
	}

	@Override
	public void onActivate(World world) {
		EntityPlayer player = world.getPlayer();

		for (int i = 0; i < 4; i++) {
			createParticle(world, player.x + (player.sizex / 4f), player.y + (player.sizey / 10f)
					+ (i * 8));

			createParticle(world, player.x + ((player.sizex / 4f) * 3), player.y
					+ (player.sizey / 10f) + (i * 8));
		}
		
		if(player.health < player.maxhealth){
			// 25% regen boost
			player.health += (Gdx.graphics.getRawDeltaTime());
			player.health = MathUtils.clamp(player.health, 0f, player.maxhealth);
		}
	}

	private void createParticle(World world, float x, float y) {
		world.particles.add(ParticlePool.obtain().setPosition(x, y).setVelocity(0, PARTICLE_SPEED)
				.setTint(getColor()).setTexture("magnetglow").setLifetime(15)
				.setDestroyOnBlock(true).setRotation(0.5f, Main.getRandom().nextBoolean())
				.setAlpha(1f));
	}

	@Override
	public void onActivateEnd(World world) {
		world.getPlayer().gravityCoefficient = lastGravCoeff;
	}

	@Override
	public void onActivateTick(World world) {

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
		return world.getPlayer().getBlockCollidingDown() == null;
	}

}
