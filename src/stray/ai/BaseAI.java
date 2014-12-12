package stray.ai;

import stray.entity.Entity;


public abstract class BaseAI {

	Entity e;
	public BaseAI(Entity e){
		this.e = e;
	}
	
	public abstract void tickUpdate();
	
	public abstract void renderUpdate();
	
}
