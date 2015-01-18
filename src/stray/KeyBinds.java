package stray;


public class KeyBinds {
	
	public static int getKey(String id, int defValue){
		return Settings.getPreferences().getInteger(id, defValue);
	}
	
	public static void setKey(String id, int key){
		Settings.getPreferences().putInteger(id, key).flush();
	}
	
}
