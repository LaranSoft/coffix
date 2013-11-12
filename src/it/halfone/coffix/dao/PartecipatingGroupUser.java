package it.halfone.coffix.dao;

public class PartecipatingGroupUser implements Comparable<PartecipatingGroupUser>{

	private String username;
	private String displayName;
	
	private long coffees;

	public PartecipatingGroupUser() {
	}
	
	public PartecipatingGroupUser(String username, String displayName) {
		this.username = username;
		this.displayName = displayName;
		this.coffees = 0;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getCoffees() {
		return coffees;
	}

	public void setCoffees(long coffees) {
		this.coffees = coffees;
	}

	@Override
	public int compareTo(PartecipatingGroupUser other) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
		if (this == other) return EQUAL;
		
		if(this.coffees > other.coffees) return AFTER;
		if(this.coffees < other.coffees) return BEFORE;
		
		return this.username.compareTo(other.username);
	}
}
