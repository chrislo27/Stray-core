package stray.augment;

import stray.Main;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;


public abstract class Augment {
	
	public static final Color reused = new Color(1, 1, 1, 1);
	
	/**
	 * called when the player just presses the activate button (key just pressed)
	 */
	public abstract void onActivateStart(World world);
	
	/**
	 * called every frame when the activate button is DOWN
	 */
	public abstract void onActivate(World world);
	
	/**
	 * called when the player releases the activate button
	 */
	public abstract void onActivateEnd(World world);
	
	/**
	 * called every tick when the activate button is down
	 */
	public abstract void onActivateTick(World world);
	
	public abstract String getName();
	
	public abstract long getUseTime();
	
	public abstract boolean canUse(World world);
	
	public Color getColor(){
		return reused.set(1, 1, 1, 1);
	}
	
	public void renderUi(Main main, World world){
		
	}
}
