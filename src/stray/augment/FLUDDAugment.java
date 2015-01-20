package stray.augment;

import stray.Main;
import stray.entity.EntityPlayer;
import stray.util.ParticlePool;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;

public class FLUDDAugment extends Augment {

	public static final float PARTICLE_SPEED = 16f;
	
	private long lastUse = System.currentTimeMillis();

	@Override
	public void onActivateStart(World world) {
		lastUse = System.currentTimeMillis();
		world.getPlayer().veloy = 0;
	}

	@Override
	public void onActivate(World world) {
		EntityPlayer player = world.getPlayer();
		
		createParticle(world, player.x + (player.sizex / 4f), player.y + (player.sizey / 10f));
		createParticle(world, player.x + ((player.sizex / 4f) * 3), player.y + (player.sizey / 10f));
		
		player.anchored = true;
	}

	private void createParticle(World world, float x, float y) {
		world.particles.add(ParticlePool.obtain().setPosition(x, y).setVelocity(0, PARTICLE_SPEED)
				.setTint(getColor()).setTexture("magnetglow").setLifetime(15).setDestroyOnBlock(true));
	}

	@Override
	public void onActivateEnd(World world) {
		world.getPlayer().anchored = false;
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
