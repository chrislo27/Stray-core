package stray.blocks;

import com.badlogic.gdx.math.MathUtils;

import stray.Main;
import stray.util.MathHelper;
import stray.util.ParticlePool;
import stray.world.World;

public class BlockGearCollectible extends BlockCollectible {

	public BlockGearCollectible(String path) {
		super(path, collectibleName);
	}

	public static final String collectibleName = "gears";

	@Override
	public void render(World world, int x, int y) {
		if (world.getMeta(x, y) != 0) return;

		super.renderWithOffset(
				world,
				x,
				y,
				0,
				(World.tilesizey / 8f)
						* ((MathHelper.clampNumberFromTime(System.currentTimeMillis()
								+ (2500 - ((x % 4) * 625)), 2.5f) * 2f) - 0.5f));
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		super.tickUpdate(world, x, y);

		if (world.tickTime % 2 == 0) return;

		world.particles.add(ParticlePool
				.obtain()
				.setTexture("checkpoint")
				.setPosition(x + 0.5f + MathUtils.random(-0.25f, 0.25f),
						y + 0.5f + MathUtils.random(-0.25f, 0.25f)).setStartScale(0.2f)
				.setEndScale(0.1f).setLifetime(0.5f).setAlpha(0.25f)
				.setVelocity(MathUtils.random(-0.5f, 0.5f), -MathUtils.random(0.5f, 1.1f)));
	}

}
