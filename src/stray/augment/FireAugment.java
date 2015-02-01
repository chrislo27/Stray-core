package stray.augment;

import com.badlogic.gdx.graphics.Color;

import stray.world.World;


public class FireAugment extends Augment{

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
	}

	@Override
	public String getName() {
		return "augment.name.fire";
	}
	
	@Override
	public Color getColor() {
		return Augment.reused.set(1, 102 / 255f, 0, 1);
	}

	@Override
	public long getUseTime() {
		return Long.MAX_VALUE;
	}

	@Override
	public boolean canUse(World world) {
		return world.getPlayer().health > 0f;
	}

	@Override
	public boolean isInUse(World world) {
		return world.getPlayer().health > 0f;
	}

}
