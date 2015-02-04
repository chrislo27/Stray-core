package stray;

public class LevelData {

	public String name = "level.unknown";
	public String cutscene = null;
	public long bestTime = Long.MAX_VALUE;

	public int difficulty = 0;

	public LevelData(String name) {
		this.name = name;
	}

	public LevelData setCutscene(String c) {
		cutscene = c;
		return this;
	}
	
	public LevelData setTime(long t){
		bestTime = t;
		return this;
	}

	public static final int TUTORIAL = 4;
	public static final int EASY = 0;
	public static final int NORMAL = 1;
	public static final int HARD = 2;
	public static final int INSANE = 3;
}
