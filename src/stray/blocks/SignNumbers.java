package stray.blocks;

import java.util.HashMap;

/**
 * correlates an integer with a string for BlockReadable
 * 
 *
 */
public class SignNumbers {
	
	private static SignNumbers instance;

	private SignNumbers() {
	}

	public static SignNumbers instance() {
		if (instance == null) {
			instance = new SignNumbers();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<Integer, String> map = new HashMap<Integer, String>();
	
	private void loadResources() {
		map.put(0, "missing_sign");
	}
	
}
