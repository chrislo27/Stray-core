package stray.entity;

import stray.Difficulty;
import stray.Main;
import stray.ai.BaseAI;
import stray.entity.types.Enemy;
import stray.entity.types.Weighted;
import stray.util.AssetMap;
import stray.util.MathHelper;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EntityPlayer extends EntityLiving implements Weighted {

	public EntityPlayer(World world, float x, float y) {
		super(world, x, y);

	}

	private float portalglow = 0;
	private float colourFade = 0f;
	public String currentSprite = "player";
	private String lastSprite = "player";


	@Override
	public void prepare() {
		sizex = 1f - (World.tilepartx * 3);
		sizey = 1f - (World.tileparty * 3);
		jump = MathHelper.getJumpVelo(world.gravity, 2.5f);
		this.maxspeed = 5f;
		this.accspeed = this.maxspeed - 0.5f;
		hasEntityCollision = true;
		maxhealth = Difficulty.get().get(
				world.main.progress.getInteger("difficulty", Difficulty.NORMAL_ID)).health;
		health = maxhealth;
	}

	public void drawSprite(float x, float y) {
		Texture sprite = world.main.manager.get(AssetMap.get(currentSprite), Texture.class);

		if (colourFade > 0) {
			world.batch.setColor(1, 1, 1, 1f - colourFade);
			drawSpriteWithFacing(sprite, x, y);

			sprite = world.main.manager.get(AssetMap.get(lastSprite), Texture.class);
			world.batch.setColor(1, 1, 1, colourFade);
			drawSpriteWithFacing(sprite, x, y);
		} else {
			sprite = world.main.manager.get(AssetMap.get(currentSprite), Texture.class);
			drawSpriteWithFacing(sprite, x, y);
		}

	}

	@Override
	public boolean damage(int hp) {
		int originalhealth = health;
		if (super.damage(hp)) {
			if (hp > 0 && invincibility == invulnTime * Main.TICKS) {
				world.renderer.onDamagePlayer(originalhealth);
				if (health == 0) {
					world.canRespawnIn = Main.TICKS * 2;
					invincibility = world.canRespawnIn - 1;
					poof();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void renderSelf(float x, float y) {
		if (health <= 0) return;

		drawSprite(x, y);
		if (colourFade > 0) {
			colourFade -= Gdx.graphics.getDeltaTime();
			if (colourFade < 0) colourFade = 0;
		}

		if (portalglow > 0) {
			world.batch.setColor(Main.getRainbow(0.5f));
			world.batch.setColor(world.batch.getColor().r, world.batch.getColor().b,
					world.batch.getColor().g, portalglow);
			world.batch.draw(world.main.animations.get("portal").getCurrentFrame(), 0, 0,
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			world.batch.setColor(1, 1, 1, 1);
			portalglow -= Gdx.graphics.getDeltaTime() * 0.75f;
			if (portalglow < 0) portalglow = 0;
		}

	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
	}

	@Override
	public boolean onDeath() {

		return true;
	}

	@Override
	public void moveLeft() {
		super.moveLeft();
	}

	public void moveRight() {
		super.moveRight();
	}

	@Override
	public BaseAI getNewAI() {
		return null;
	}

}
