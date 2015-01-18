package stray;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Settings {
	
	private static Settings instance;

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
	
}
