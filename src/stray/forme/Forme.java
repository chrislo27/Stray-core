package stray.forme;

import stray.entity.Entity;

import com.badlogic.gdx.graphics.Texture;


public abstract class Forme {

	public String name = "";
	protected Entity e;
	public Forme(String n, Entity e){
		name = n;
		this.e = e;
	}
	
	public abstract void tickUpdate();
	
	public abstract void render();
	
	public abstract void activate();
	
	public abstract String getSprite();
	
	/**
	 * @return null if it should not display
	 */
	public abstract String getActionText();
	
	public abstract String getUiTexture();
	
	public boolean allowDamage(){
		return true;
	}
}
