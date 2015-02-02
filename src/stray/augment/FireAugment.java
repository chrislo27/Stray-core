package stray.augment;

import stray.Main;
import stray.entity.EntityPlayer;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class FireAugment extends Augment {

	private static final float DPS = (1f / 6.4f);

	@Override
	public void onActivateStart(World world) {
	}

	@Override
	public void onActivate(World world) {
		EntityPlayer player = world.getPlayer();
		
		player.heal(-(DPS * Gdx.graphics.getDeltaTime())
				+ -(Gdx.graphics.getDeltaTime() / EntityPlayer.SECONDS_TO_REGEN));
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
