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
	boolean direction = Main.getRandom().nextBoolean();

	@Override
	public void tickUpdate() {

	}

	@Override
	public void renderUpdate() {
		if (e.getBlockCollidingLeft() != null && e.getBlockCollidingRight() != null){
			return;
		}

		if (direction) {
			e.moveRight();
		} else {
			e.moveLeft();
		}
		
		if(direction){
			if(e.getBlockCollidingRight() != null) direction = false;
		}else{
			if(e.getBlockCollidingLeft() != null) direction = true;
		}
	}

}
