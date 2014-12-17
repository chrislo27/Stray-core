package stray.effect;

import java.util.concurrent.TimeUnit;

import stray.Main;
import stray.world.World;

public class Effect {

	protected int timer = 0;
	protected int lifetime = 0;
	public String name = "";
	public String texture = "";
	public boolean hidden = false;

	public Effect(int time) {
		timer = time;
	}

	public void tickUpdate() {
		lifetime++;
		timer--;

	}

	public void render(World world) {

	}

	public int getTimer() {
		return timer;
	}

	public int getLifetime() {
		return lifetime;
	}

	public Effect hide() {
		hidden = true;
		return this;
	}

	public String getLocalizedTime() {
		if (timer >= Main.TICKS) {
			int millis = timer * (Math.round(1000f / Main.TICKS));
			return String.format(
					"%d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(millis),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		} else {
			return String.format("%.1f", (timer * 1f) / Main.TICKS);
		}
	}
}
