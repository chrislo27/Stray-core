package stray.util;

import java.util.HashMap;


public class GlobalVariables {

	
	private HashMap<String, String> variables = new HashMap<String, String>();
	
	public void setValue(String key, String value){
		variables.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @return value or ""
	 */
	public String getValue(String key){
		if(!variables.containsKey(key)){
			return "";
		}else{
			return variables.get(key);
		}
	}
	
	public void clear(){
		variables.clear();
	}
}
