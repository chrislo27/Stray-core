package stray.ai;

import stray.Main;
import stray.entity.Entity;

public class AIDumbEnemy extends BaseAI {

	public AIDumbEnemy(Entity e) {
		super(e);
	}

	/**
	 * false = left
	 */
	boolean direction = Main.getRandomInst().nextBoolean();

	@Override
	public void tickUpdate() {

	}

	@Override
	public void renderUpdate() {
		if (e.getBlockCollidingLeft() != null && e.getBlockCollidingRight() != null) return;

		if (direction) {
			e.moveRight();
		} else {
			e.moveLeft();
		}
		if (e.getBlockCollidingLeft() != null && !direction) {
			direction = true;
		} else if (e.getBlockCollidingRight() != null && direction) {
			direction = false;
		}
	}

}
