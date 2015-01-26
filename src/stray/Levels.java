package stray;

import java.util.HashMap;

public class Levels {
	
	public static final int LEVELS_PER_CHAPTER = 8;
	
	private static Levels instance;

	private Levels() {
	}

	public static Levels instance() {
		if (instance == null) {
			instance = new Levels();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<Integer, LevelData> levels = new HashMap<Integer, LevelData>();
	public HashMap<LevelData, Integer> reverse = new HashMap<LevelData, Integer>();
	
	private void loadResources() {
		levels.clear();
		
		add(new LevelData("level1-1").setCutscene("controls"));
		add(new LevelData("level1-2"));
		add(new LevelData("level1-3"));
		add(new LevelData("level1-4"));
		add(new LevelData("level1-5"));
		add(new LevelData("level1-6"));
		add(new LevelData("level1-7"));
		add(new LevelData("level1-8"));
	}
	
	private int num = 0;
	
	private void add(LevelData l){
		levels.put(num, l);
		reverse.put(l, num);
		num++;
	}
	
	public static String getLevelName(int level, int groupnum){
		return ((level / groupnum) + 1) + "-" + ((level % groupnum) + 1);
	}
}
