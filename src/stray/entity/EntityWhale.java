package stray.entity;

import stray.ai.AIDumbEnemy;
import stray.ai.BaseAI;
import stray.entity.types.Enemy;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;


public class EntityWhale extends EntityLiving implements Enemy{

	public EntityWhale(World w, float posx, float posy) {
		super(w, posx, posy);
	}

	@Override
	public float getDamageDealt() {
		return 0;
	}

	@Override
	public void prepare() {
		super.prepare();
		sizex = 10f;
		sizey = 5f;
		this.maxspeed = 2f;
		this.accspeed = maxspeed * maxspeed;
		this.hasEntityCollision = false;
		gravityCoefficient = 0;
	}

	@Override
	public void renderSelf(float x, float y) {
		this.drawSpriteWithFacing(
				world.main.manager.get(AssetMap.get("entitywhale"), Texture.class), x, y);
	}

	@Override
	public BaseAI getNewAI() {
		return new AIDumbEnemy(this);
	}

}
