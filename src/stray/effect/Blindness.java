package stray.effect;

import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Blindness extends Effect {

	public Blindness(int time) {
		super(time);
		texture = "effecticonblindness";
		name = "Blindness";

	}

	@Override
	public void render(World world) {
		float a = 1;
		if (lifetime <= 40) {
			a = lifetime / 40f;
		}
		if (timer <= 40) {
			a = timer / 40f;
		}
		world.batch.setColor(1, 1, 1, a);
		world.batch.draw(
				world.main.manager.get(AssetMap.get("effectoverlayblindness"), Texture.class), 0,
				0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		world.batch.setColor(1, 1, 1, 1);
	}

}
