package stray.augment;

import com.badlogic.gdx.graphics.Color;


public abstract class Augment {
	
	public static final Color reused = new Color(1, 1, 1, 1);
	
	/**
	 * called when the player just presses the activate button (key just pressed)
	 */
	public abstract void onActivateStart();
	
	/**
	 * called every frame when the activate button is DOWN
	 */
	public abstract void onActivate();
	
	/**
	 * called when the player releases the activate button
	 */
	public abstract void onActivateEnd();
	
	public abstract String getName();
	
	public Color getColor(){
		return reused.set(1, 1, 1, 1);
	}
}
