package stray.augment;

import stray.util.Direction;
import stray.util.Particle;
import stray.util.ParticlePool;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;

public class TelekinesisAugment extends Augment {

	@Override
	public void onActivateStart(World world) {
	}

	@Override
	public void onActivate(World world) {

	}

	@Override
	public void onActivateEnd(World world) {
	}

	@Override
	public void onActivateTick(World world) {
		world.particles.add(ParticlePool.obtain()
				.setPosition(world.getPlayer().x + world.getPlayer().sizex / 2f, world.getPlayer().y + world.getPlayer().sizey / 2f).setLifetime(30)
				.setDestroyOnBlock(true).setGravity(world.gravity).setTexture("magnetglow")
				.setVelocity(10f * (world.getPlayer().facing == Direction.LEFT ? -1 : 1), -10f));
	}

	@Override
	public String getName() {
		return "augment.name.telekinesis";
	}

	@Override
	public long getUseTime() {
		return 2500;
	}

	@Override
	public boolean canUse(World world) {
		return true;
	}

	@Override
	public boolean isInUse(World world) {
		return true;
	}

	@Override
	public Color getColor() {
		return Augment.reused.set(1, 102 / 255f, 0, 1);
	}

}
