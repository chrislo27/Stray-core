package stray;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Settings {
	
	private static Settings instance;
	
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	
	public static boolean showFPS = true;
	public static boolean debug = false;
	public static boolean showVignette = true;

	private Settings() {
	}

	public static Settings instance() {
		if (instance == null) {
			instance = new Settings();
			instance.loadResources();
		}
		return instance;
	}

	private Preferences pref;
	
	private void loadResources() {
		pref = Main.getPref("settings");
		
		showFPS = pref.getBoolean("showFPS", true);
	}
	
	public static Preferences getPreferences(){
		return instance().pref;
	}
	
	public static float getMusicVolume(){
		return instance().pref.getFloat("music", 1);
	}
	
	public static float getSoundVolume(){
		return instance().pref.getFloat("sound", 1);
	}
	
}
