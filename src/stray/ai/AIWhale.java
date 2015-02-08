package stray.ai;

import com.badlogic.gdx.math.MathUtils;

import stray.entity.Entity;
import stray.entity.EntityFlame;
import stray.entity.EntityLiving;
import stray.util.Direction;

public class AIWhale extends AIDumbEnemy {

	public AIWhale(Entity e) {
		super(e);
	}

	@Override
	public void renderUpdate() {
		super.renderUpdate();
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();

		if (Math.abs(e.world.getPlayer().x - (e.x + e.sizex)) < 7.5f) {
			if (((EntityLiving) e).facing == Direction.RIGHT) {
				if (e.world.getPlayer().x >= e.x + e.sizex) {
					EntityFlame flame = new EntityFlame(e.world, e.x + 8.703125f, e.y + 3.9375f);
					flame.velox = (e.world.getPlayer().x - flame.x) * 2f;
					flame.veloy = ((e.world.getPlayer().y + e.world.getPlayer().sizex) - flame.y) * 1.25f;

					e.world.entities.add(flame);
				}
			} else if (((EntityLiving) e).facing == Direction.LEFT) {
				if (e.world.getPlayer().x + e.world.getPlayer().sizex <= e.x) {
					EntityFlame flame = new EntityFlame(e.world, e.x + 0.875f, e.y + 3.9375f);
					flame.velox = (e.world.getPlayer().x - flame.x) * 2f;
					flame.veloy = ((e.world.getPlayer().y + e.world.getPlayer().sizex) - flame.y) * 1.25f;

					e.world.entities.add(flame);
				}
			}
		}
	}

}
