package stray.entity;

import stray.Main;
import stray.ai.BaseAI;
import stray.augment.Augments;
import stray.entity.types.Enemy;
import stray.entity.types.Stunnable;
import stray.entity.types.Weighted;
import stray.util.AssetMap;
import stray.util.DamageSource;
import stray.util.Difficulty;
import stray.util.Direction;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.util.render.ElectricityRenderer;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class EntityPlayer extends EntityLiving implements Weighted, Stunnable {

	public EntityPlayer(World world, float x, float y) {
		super(world, x, y);

	}

	public static final float SECONDS_TO_REGEN = 15;

	@Override
	public void prepare() {
		sizex = 1f - (World.tilepartx * 3);
		sizey = 1f - (World.tileparty * 3);
		jump = MathHelper.getJumpVelo(world.gravity, 2.5f);
		this.maxspeed = 5f;
		this.accspeed = maxspeed * 5f;
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
		world.batch.setColor(Augments.getAugment(world.currentAugment).getColor().r, Augments
				.getAugment(world.currentAugment).getColor().g,
				Augments.getAugment(world.currentAugment).getColor().b, world.batch.getColor().a);
		
		if (world.getAugmentsUnlocked() <= 0) world.batch.setColor(1, 1, 1, 1);
		if (facing == Direction.LEFT) {
			Utils.drawRotated(world.batch, world.main.textures.get("gear"), x + 5, y - 29, 26, 26,
					MathHelper.getNumberFromTime(((world.augmentActivate) ? 0.75f : 5f)) * 360,
					false);
			Utils.drawRotated(world.batch, world.main.textures.get("gear"), x + 32, y - 25, 19, 19,
					MathHelper.getNumberFromTime(((world.augmentActivate) ? 0.75f : 5f)) * 360,
					true);
		} else if (facing == Direction.RIGHT) {
			Utils.drawRotated(world.batch, world.main.textures.get("gear"), x + 31, y - 29, 26, 26,
					MathHelper.getNumberFromTime(((world.augmentActivate) ? 0.75f : 5f)) * 360,
					true);
			Utils.drawRotated(world.batch, world.main.textures.get("gear"), x + 12, y - 25, 19, 19,
					MathHelper.getNumberFromTime(((world.augmentActivate) ? 0.75f : 5f)) * 360,
					false);
		}
		world.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public boolean damage(float dmg, DamageSource source) {
		float originalhealth = health;
		if (super.damage(dmg * Difficulty.get().get(world.main.getDifficulty()).damageMultiplier,
				source)) {
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

	private long lastBadHeal = System.currentTimeMillis();

	@Override
	public void heal(float amt, DamageSource source) {
		super.heal(amt, source);
		if (amt < 0) {
			if (System.currentTimeMillis() - lastBadHeal >= 500) {
				lastBadHeal = System.currentTimeMillis();
				world.vignettecolour.set(1, 0, 0, 0.25f);
			}
		}
	}

	@Override
	public void renderSelf(float x, float y) {
		if (health <= 0) {
			if (world.canRespawnIn > (Main.TICKS * 2f) - (Main.TICKS * 0.5f)) {
				for (int i = 0; i < MathUtils.random(1, 3); i++) {
					ElectricityRenderer.drawP2PLightning(world.batch, (x + (sizex / 2f)
							* World.tilesizex), (y - (sizey / 2f) * World.tilesizey),
							((x + (sizex / 2f) * World.tilesizex))
									+ (World.tilesizex * sizex * MathUtils.random(-1f, 1f)),
							((y - (sizey / 2f) * World.tilesizey))
									+ (World.tilesizex * sizex * MathUtils.random(-1f, 1f)), 24,
							1.5f, 2, 3, Color.CYAN.toFloatBits());
				}
			}
			return;
		}

		drawSprite(x, y);

	}

	@Override
	public void renderUpdate() {
		super.renderUpdate();
		if (invincibility == 0) {
			if (health < maxhealth) {
				health += (Gdx.graphics.getDeltaTime() / SECONDS_TO_REGEN);
				health = MathUtils.clamp(health, 0, maxhealth);
			}
		}
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();

		if (fireTime > 2) fireTime = 2;

		for (Entity e : world.entities) {
			if (e instanceof Enemy) {
				if (this.intersectingOther(e)) {
					this.damage(((Enemy) e).getDamageDealt(), DamageSource.generic);
					break;
				}
			}
		}
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
