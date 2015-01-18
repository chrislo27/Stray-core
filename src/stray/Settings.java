package stray;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Settings {
	
	private static Settings instance;
	
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	public static final int SMALLER_WIDTH = 864;
	public static final int SMALLER_HEIGHT = 648;

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
	}
	
	public static Preferences getPreferences(){
		return instance().pref;
	}
	
	// SETTINGS METHODS TO END OF FILE
	
	public static boolean isSmallResolution(){
		return instance().pref.getBoolean("resolutionsmall", false);
	}
	
}
