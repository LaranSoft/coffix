package it.halfone.coffix.configuration;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private static Configuration instance;

	private Map<String, String> values;
	
	private Configuration(){
		values = new HashMap<>();
	}
	
	public static Configuration getInstance(){
		if(instance == null){
			instance = new Configuration();
		}
		return instance;
	}
	
	public void put(String key, String value){
		values.put(key, value);
	}
	
	public String get(String key){
		return values.get(key);
	}

	public void clear() {
		values.clear();
	}

}
