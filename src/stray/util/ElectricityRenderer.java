package stray.util;

import stray.Main;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ElectricityRenderer {

	// do a default of thickness = 3f
	// default number of bolts = 3

	private static Vector2 tempSphereVector = new Vector2(0, 0);

	public static void drawLine(SpriteBatch batch, float _x1, float _y1, float _x2, float _y2,
			float thickness) {
		float length = (float) MathHelper.calcDistance(_x1, _y1, _x2, _y2);
		float dx = _x1;
		float dy = _y1;
		dx = dx - _x2;
		dy = dy - _y2;
		float angle = MathUtils.radiansToDegrees * MathUtils.atan2(dy, dx);
		angle = angle - 180;
		batch.draw(Main.filltex, _x1, _y1, 0f, thickness * 0.5f, length, thickness, 1f, 1f, angle,
				0, 0, 1, 1, false, false);
	}

	public static void drawLine(SpriteBatch batch, float _x1, float _y1, float _x2, float _y2,
			float thickness, Texture tex) {
		float length = (float) MathHelper.calcDistance(_x1, _y1, _x2, _y2);
		float dx = _x1;
		float dy = _y1;
		dx = dx - _x2;
		dy = dy - _y2;
		float angle = MathUtils.radiansToDegrees * MathUtils.atan2(dy, dx);
		angle = angle - 180;
		batch.draw(tex, _x1, _y1, 0f, thickness * 0.5f, length, thickness, 1f, 1f, angle, 0, 0,
				tex.getWidth(), tex.getHeight(), false, false);
	}

	public static void drawChainLightning(SpriteBatch batch, Vector2[] points, float thickness,
			int numberOfBolts) {
		for (int i = 0; i < points.length - 1; i++) {
			drawP2PLightning(batch, points[i].x, points[i].y, points[i + 1].x, points[i + 1].y,
					Main.random(60f, 140f), Main.random(0.8f, 3.8f), thickness, numberOfBolts);
		}
	}

	public static void drawP2ALightning(SpriteBatch batch, float x1, float y1, float x2, float y2,
			float displace, float detail, float thickness, float noise, int numberOfBolts) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		for (int i = 0; i < numberOfBolts; i++) {
			batch.setColor(Main.random(14f, 54f) / 255f, Main.random(100f, 210f) / 255f,
					Main.random(200f, 239f) / 255f, 1f);
			drawSingleP2PLightning(batch, x1, y1, x2 + Main.random(-noise, noise),
					y2 + Main.random(-noise, noise), 117, 1.8f, thickness);
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void drawP2PLightning(SpriteBatch batch, float x1, float y1, float x2, float y2,
			float displace, float detail, float thickness, int numberOfBolts) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		for (int i = 0; i < numberOfBolts; i++) {
			batch.setColor(Main.random(14f, 54f) / 255f, Main.random(100f, 210f) / 255f,
					Main.random(200f, 239f) / 255f, 1f);
			drawSingleP2PLightning(batch, x1, y1, x2, y2, 117, 1.8f, thickness);
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void drawSingleP2PLightning(SpriteBatch batch, float x1, float y1, float x2,
			float y2, float displace, float detail, float thickness) {
		if (displace < detail) {
			drawLine(batch, x1, y1, x2, y2, thickness, Main.filltex);
		} else {
			float mid_x = (x2 + x1) * 0.5f;
			float mid_y = (y2 + y1) * 0.5f;
			mid_x += (Math.random() - 0.5f) * displace;
			mid_y += (Math.random() - 0.5f) * displace;
			drawSingleP2PLightning(batch, x1, y1, mid_x, mid_y, displace * 0.5f, detail, thickness);
			drawSingleP2PLightning(batch, x2, y2, mid_x, mid_y, displace * 0.5f, detail, thickness);
		}
	}

}
