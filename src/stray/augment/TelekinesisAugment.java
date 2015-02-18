package stray.augment;

import com.badlogic.gdx.graphics.Color;

import stray.Settings;
import stray.util.render.PostProcessing;
import stray.world.World;

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
