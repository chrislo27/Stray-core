package stray.blocks;

import java.util.HashMap;

/**
 * correlates a block metadata integer with a string
 * 
 *
 */
public class MetaStrings {
	
	private static MetaStrings instance;

	private MetaStrings() {
	}

	public static MetaStrings instance() {
		if (instance == null) {
			instance = new MetaStrings();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<Integer, String> map = new HashMap<Integer, String>();
	
	private void loadResources() {
		map.put(0, "missing_sign");
	}
	
}
