package stray.augment;


public abstract class Augment {
	
	
	/**
	 * called when the player presses the activate button (key just pressed)
	 */
	public abstract void onJustActivate();
	
	/**
	 * called every frame when the activate button is DOWN
	 */
	public abstract void onHoldActivate();
	
}
