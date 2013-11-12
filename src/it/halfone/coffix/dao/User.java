package it.halfone.coffix.dao;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 73350739413384659L;
	private String baseName;
	private String username;
	
	private String entityKey;
	
	/**
	 * @param baseName
	 */
	public User() {}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEntityKey() {
		return entityKey;
	}

	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
}
