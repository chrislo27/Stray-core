package stray.transition;

import stray.Main;
import stray.util.render.StencilMaskUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GearTransition implements Transition {

	public static final float GEAR_MIDDLE_DIAMETER = 178f;

	float gearscale = 0f;
	float seconds = 1;

	Texture nextScreen = null;

	boolean forcefinish = false;

	public GearTransition(float seconds) {
		this.seconds = seconds;
	}

	@Override
	public boolean finished() {
		return gearscale >= 1 || forcefinish;
	}

	@Override
	public void render(Main main) {
		if (nextScreen == null) {
			if (Main.TRANSITION.nextScreen == null) {
				forcefinish = true;
				return;
			}
			main.batch.end();
			main.buffer2.begin();
			Main.TRANSITION.nextScreen.render(Gdx.graphics.getDeltaTime());
			main.buffer2.end();
			nextScreen = main.buffer2.getColorBufferTexture();
			main.batch.begin();
		}

		if (nextScreen == null) return;

		Texture gear = main.textures.get("gear");
		float size = (gearscale * (Gdx.graphics.getWidth() / GEAR_MIDDLE_DIAMETER) * Gdx.graphics
				.getWidth() / 2);

		main.batch.setColor(1, 1, 1, 1);
		main.batch.draw(gear, (Gdx.graphics.getWidth() / 2) - (size / 2f),
				(Gdx.graphics.getHeight() / 2) - (size / 2f), size, size);

		main.batch.end();
		StencilMaskUtil.prepareMask();

		main.shapes.begin(ShapeType.Filled);
		main.shapes
				.circle(Gdx.graphics.getWidth() / 2,
						Gdx.graphics.getHeight() / 2,
						(((GEAR_MIDDLE_DIAMETER / gear.getWidth()) * gearscale) * (Gdx.graphics.getWidth() / GEAR_MIDDLE_DIAMETER) * Gdx.graphics
								.getWidth()) / 2 / 2);
		main.shapes.end();
		
		main.batch.begin();
		StencilMaskUtil.useMask();
		main.batch.draw(nextScreen, 0, Gdx.graphics.getHeight(), nextScreen.getWidth(),
				-nextScreen.getHeight());

		gearscale += Gdx.graphics.getDeltaTime() / seconds;
		main.batch.flush();
		StencilMaskUtil.resetMask();
	}

	@Override
	public void tickUpdate(Main main) {
	}

}
