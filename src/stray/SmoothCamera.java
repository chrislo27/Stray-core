package stray;

import stray.world.World;

import com.badlogic.gdx.Gdx;


public class SmoothCamera {

	public float camerax = 0;
	public float cameray = 0;
	
	float velox = 0;
	float veloy = 0;
	
	public float wantedx = 0;
	public float wantedy = 0;
	
	private transient World world;
	
	public float SPEED = 7.5f;
	
	private float shakeTime = 0;
	private float shakeIntensity = 1f;
	private boolean shakeFades = false;
	
	public SmoothCamera(World w){
		world = w;
	}
	
	/**
	 * 
	 * @param time
	 * @param intensity float in block sizes
	 */
	public void shake(float time, float intensity, boolean fades){
		shakeTime = time;
		shakeIntensity = intensity;
		shakeFades = fades;
	}
	
	/**
	 * call once per render, before rendering thingies
	 */
	public void update(){
		if(Math.abs(wantedx - camerax) > 0.005f){
			velox = (float) ((SPEED) * ((wantedx - camerax)));
			camerax += velox * Gdx.graphics.getDeltaTime();
		}else{
			velox = 0;
			camerax = wantedx;
		}
		if(Math.abs(wantedy - cameray) > 0.005f){
			veloy = (float) ((SPEED) * ((wantedy - cameray)));
			cameray += veloy * Gdx.graphics.getDeltaTime();
		}else{
			veloy = 0;
			cameray = wantedy;
		}
		clamp();
		world.main.camera.update();
		if(shakeTime > 0){
			shakeTime -= Gdx.graphics.getDeltaTime();
			if(shakeTime < 0){
				shakeTime = 0;
				if(shakeFades){
					shake(0.25f, shakeIntensity / 5f, false);
				}
			}
		}
	}
	
	public void centerOn(float x, float y){
		wantedx = x - (Gdx.graphics.getWidth() / 2f);
		wantedy = y - (Gdx.graphics.getHeight() / 2f);
		if(shakeTime > 0){
			wantedx += Main.random(-Math.round((shakeIntensity * World.tilesizex) * 10), Math.round(shakeIntensity * World.tilesizey * 10)) / 10f;
			wantedy += Main.random(-Math.round((shakeIntensity * World.tilesizex) * 10), Math.round(shakeIntensity * World.tilesizey * 10)) / 10f;
		}
		clamp();
	}
	
	public void forceCenterOn(float x, float y){
		wantedx = x - (Gdx.graphics.getWidth() / 2f);
		wantedy = y - (Gdx.graphics.getHeight() / 2f);
		camerax = wantedx;
		cameray = wantedy;
		velox = 0;
		veloy = 0;
		clamp();
	}
	
	public void clamp() {
		if (camerax < 0) camerax = 0;
		if (cameray < 0) cameray = 0;

		if (camerax + (Gdx.graphics.getWidth()) > world.sizex * World.tilesizex) {
			camerax = world.sizex * World.tilesizex - Gdx.graphics.getWidth();
		}
		if (cameray + (Gdx.graphics.getHeight()) > world.sizey * World.tilesizey) {
			cameray = world.sizey * World.tilesizey - Gdx.graphics.getHeight();
		}
	}
}
