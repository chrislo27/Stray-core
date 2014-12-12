package stray.forme;

import stray.Main;
import stray.Particle;
import stray.ParticlePool;
import stray.blocks.Blocks;
import stray.entity.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class FormeTurtle extends Forme {

	public FormeTurtle(Entity e) {
		super("forme.annihilationturtle", e);
	}

	@Override
	public String getSprite() {
		return "playerturtle";
	}

	@Override
	public void render() {

	}

	@Override
	public String getActionText() {
		return "forme.action.annihilationturtle";
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void activate() {
	}

	@Override
	public String getUiTexture() {
		return "formeuiturtle";
	}
	
	

}
