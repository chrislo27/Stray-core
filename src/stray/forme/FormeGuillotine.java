package stray.forme;

import com.badlogic.gdx.graphics.Texture;

import stray.Main;
import stray.Particle;
import stray.ParticlePool;
import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.entity.EntityPlayer;
import stray.util.MathHelper;


public class FormeGuillotine extends Forme{

	public FormeGuillotine(Entity e) {
		super("forme.guillotine", e);
	}

	boolean active = false;
	private double oldy = 0;

	@Override
	public void tickUpdate() {
		if (active) {
			Entity col = e.entityColliding();
			if(col != null){
				if(col instanceof EntityLiving){
					((EntityLiving) col).damage(1);
				}
			}
			if (!e.anchored) {
				if (e.getBlockCollidingDown() == null) {
					e.veloy += (e.world.gravity / Main.TICKS) * 5;
				} else {
					active = false;
					e.world.camera.shake(0.15f,
							(float) MathHelper.clamp(Math.abs((e.y - oldy) / 5f), 0.75, 2.5), false);
				}
			}

			if(e instanceof EntityPlayer){
				Particle p = ParticlePool.obtain();
				p.setTexture(((EntityPlayer) e).currentSprite);
				p.setLifetime(0.1f);
				p.setPosition((float) (e.x + (e.sizex / 2f)), (float) e.y);
				p.setTint(1, 1, 1, 0.1f);
				e.world.particles.add(p);
			}
		}
	}

	@Override
	public void activate() {
		if (e.getBlockCollidingDown() == null) {
			active = true;
			oldy = e.y;
		}
	}
	
	@Override
	public void render() {
	}


	@Override
	public String getSprite() {
		return "playerguillotine";
	}

	@Override
	public String getActionText() {
		return "forme.action.guillotine";
	}
	
	@Override
	public boolean allowDamage(){
		return !active;
	}

	@Override
	public String getUiTexture() {
		return "formeuiguillotine";
	}

}
