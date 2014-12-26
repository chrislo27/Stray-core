package stray.augment;


public abstract class Augment {
	
	
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
	
}
