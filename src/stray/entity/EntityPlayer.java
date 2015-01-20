package stray.entity;

import stray.Main;
import stray.ai.BaseAI;
import stray.augment.Augments;
import stray.entity.types.Weighted;
import stray.util.AssetMap;
import stray.util.Difficulty;
import stray.util.Direction;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class EntityPlayer extends EntityLiving implements Weighted {

	public EntityPlayer(World world, float x, float y) {
		super(world, x, y);

	}

	private float portalglow = 0;
	public static final float SECONDS_TO_REGEN = 15;

	@Override
	public void prepare() {
		sizex = 1f - (World.tilepartx * 3);
		sizey = 1f - (World.tileparty * 3);
		jump = MathHelper.getJumpVelo(world.gravity, 2.5f);
		this.maxspeed = 5f;
		this.accspeed = this.maxspeed - 0.5f;
		hasEntityCollision = true;
		maxhealth = 1;
		health = maxhealth;
	}

	public void drawSprite(float x, float y) {
		Texture sprite = world.main.manager.get(AssetMap.get("player"), Texture.class);
		drawSpriteWithFacing(sprite, x, y);
		drawGears(x, y);
	}

	private void drawGears(float x, float y) {
		world.batch.setColor(Augments.getAugment(world.currentAugment).getColor());
		if (world.main.getAugmentsUnlocked() <= 0) world.batch
				.setColor(1, 1, 1, 1);
		if (facing == Direction.LEFT) {
			Utils.drawRotated(
					world.batch,
					world.main.textures.get("gear"),
					x + 5,
					y - 29,
					26,
					26,
					MathHelper.getNumberFromTime(((Gdx.input.isKeyPressed(Keys.E)) ? 0.75f : 5f)) * 360,
					false);
			Utils.drawRotated(
					world.batch,
					world.main.textures.get("gear"),
					x + 32,
					y - 25,
					19,
					19,
					MathHelper.getNumberFromTime(((Gdx.input.isKeyPressed(Keys.E)) ? 0.75f : 5f)) * 360,
					true);
		} else if (facing == Direction.RIGHT) {
			Utils.drawRotated(
					world.batch,
					world.main.textures.get("gear"),
					x + 31,
					y - 29,
					26,
					26,
					MathHelper.getNumberFromTime(((Gdx.input.isKeyPressed(Keys.E)) ? 0.75f : 5f)) * 360,
					true);
			Utils.drawRotated(
					world.batch,
					world.main.textures.get("gear"),
					x + 12,
					y - 25,
					19,
					19,
					MathHelper.getNumberFromTime(((Gdx.input.isKeyPressed(Keys.E)) ? 0.75f : 5f)) * 360,
					false);
		}
		world.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public boolean damage(float dmg) {
		float originalhealth = health;
		if (super.damage(dmg * Difficulty.get().get(world.main.getDifficulty()).damageMultiplier)) {
			if (dmg > 0 && invincibility == invulnTime * Main.TICKS) {
				world.renderer.onDamagePlayer(originalhealth);
				if (health == 0) {
					world.canRespawnIn = Main.TICKS * 2;
					invincibility = world.canRespawnIn + 1;
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

		if (portalglow > 0) {
			world.batch.setColor(Main.getRainbow(0.5f, 1));
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
	public void renderUpdate() {
		super.renderUpdate();
		if (invincibility == 0) {
			if (health < maxhealth) {
				health += (Gdx.graphics.getRawDeltaTime() / SECONDS_TO_REGEN);
				health = MathUtils.clamp(health, 0, maxhealth);
			}
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
