package stray.objective;

import com.badlogic.gdx.Gdx;

public class Objective {

	public int id = 0;
	private boolean completed = false;
	public long completedTime = -1;
	public float outTime = -1f;

	public static final long showTimeWhenCompleted = 2500;

	public Objective(int id) {
		this.id = id;
	}

	public Objective complete() {
		if (completed) return this;

		completedTime = System.currentTimeMillis();
		completed = true;

		return this;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void update() {
		if (outTime < 1) {
			if (!isCompleted()) {
				outTime += (1f - outTime) * Gdx.graphics.getDeltaTime() * 4f;
			} else {
				if (System.currentTimeMillis() - completedTime >= (showTimeWhenCompleted / 4f) * 3) {
					outTime += (-1f - outTime) * Gdx.graphics.getDeltaTime() * 4f;
				}
			}

			if (outTime > 1) outTime = 1;
			if (outTime < -1f) outTime = -1f;
		}
	}

}
