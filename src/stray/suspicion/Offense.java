package stray.suspicion;


public abstract class Offense {

	protected static final String defaultName = "offense.default";
	
	public String getUnlocalizedName(){
		return defaultName;
	}
	
	public int getOffenseLevel(){
		return 1;
	}
}
