package stray.offenses;


public abstract class Offense {

	private static final String defaultName = "offense.default";
	
	public String getUnlocalizedName(){
		return defaultName;
	}
	
	public int getOffenseLevel(){
		return 1;
	}
}
