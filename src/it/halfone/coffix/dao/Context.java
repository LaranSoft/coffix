package it.halfone.coffix.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Context implements Serializable{

	private static final long serialVersionUID = -7505426454517863074L;

	private String response;
	private Map<String, Object> context;
	
	public Context() {
		context = new HashMap<>();
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void put(String key, Object value){
		context.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key){
		return (T) context.get(key);
	}
}
