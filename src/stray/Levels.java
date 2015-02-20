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
		
		add(new LevelData("chapter1/level1-1", 0).setCutscene("controls"));
		add(new LevelData("chapter1/level1-2", 0));
		add(new LevelData("chapter1/level1-3", 0));
		add(new LevelData("chapter1/level1-4", 1));
		add(new LevelData("chapter1/level1-5", 1));
		add(new LevelData("chapter1/level1-6", 1));
		add(new LevelData("chapter1/level1-7", 1));
		add(new LevelData("chapter1/level1-8", 1));
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
	
	public static String getLevelName(int level){
		return getLevelName(level, LEVELS_PER_CHAPTER);
	}
	
	public int getNumFromLevelFile(String file){
		for(int i = 0; i < levels.size(); i++){
			if(levels.get(i).path.equals(file)) return i;
		}
		
		return -1;
	}
	
	public LevelData getLevelData(String file){
		for(int i = 0; i < levels.size(); i++){
			if(levels.get(i).path.equals(file)) return levels.get(i);
		}
		
		return null;
	}
}
