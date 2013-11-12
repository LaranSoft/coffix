package it.halfone.coffix.dao;

import java.util.HashMap;
import java.util.Map;

public class CofferPartecipant{
	
	private String username;
	private String displayName;
	private boolean hasNegate = false;
	
	public CofferPartecipant() {}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean hasNegate() {
		return hasNegate;
	}
	
	public void setHasNegate(boolean hasNegate) {
		this.hasNegate = hasNegate;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Map<String, String> toMap(){
		Map<String, String> retVal = new HashMap<>();
		retVal.put("username", username);
		retVal.put("hasNegate", "" + hasNegate);
		return retVal;
	}
}
