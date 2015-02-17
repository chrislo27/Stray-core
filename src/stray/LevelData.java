package stray;

public class LevelData {

	public String name = "level.unknown";
	public String cutscene = null;
	public long bestTime = Long.MAX_VALUE;
	public int augment = 0;
	public LevelType leveltype = LevelType.NORMAL;
	public int difficulty = 0;

	public LevelData(String name, int augment) {
		this.name = name;
		this.augment = augment;
	}

	public LevelData setCutscene(String c) {
		cutscene = c;
		return this;
	}
	
	public LevelData setBestTime(long t){
		bestTime = t;
		return this;
	}
	
	public LevelData setType(LevelType t){
		leveltype = t;
		return this;
	}

	public static final int TUTORIAL = 4;
	public static final int EASY = 0;
	public static final int NORMAL = 1;
	public static final int HARD = 2;
	public static final int INSANE = 3;
	
	public static enum LevelType{
		NORMAL("", "normal"), VOID("void", "void"), ;
		
		public String image = "";
		public String text = "";
		
		LevelType(String img, String txt){
			image = img;
			text = txt;
		}
	}
}
