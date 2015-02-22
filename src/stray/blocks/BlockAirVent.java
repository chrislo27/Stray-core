package stray.blocks;

import com.badlogic.gdx.math.MathUtils;

import stray.entity.Entity;
import stray.util.ParticlePool;
import stray.world.World;

public class BlockAirVent extends Block {

	public BlockAirVent(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (Block.entityIntersects(world, x - World.tilepartx, y, e, 1 + (world.tilepartx * 2), -3f)) {
				e.veloy -= 1f;
			}
		}
		
		if(world.tickTime % 2 == 0) return;
		
		world.particles.add(ParticlePool.obtain().setTexture("airwhoosh").setPosition(x + 0.5f + MathUtils.random(-0.25f, 0.25f), y)
				.setVelocity(MathUtils.random(-0.075f, 0.075f), -10f).setLifetime(0.33333333f)
				.setStartScale(2f).setEndScale(2f).setAlpha(0.5f));
	}

}
