package it.halfone.coffix.constants;

public interface Entities {

	public static class Group {
		public static final String KIND = "Group";
		
		public static class Property {
			public static final String NAME = "name";
			public static final String UUID = "uuid";
			public static final String CREATOR = "creator";
			public static final String PARTECIPATING_USERS = "userMap";
			public static final String INVITED_USER_MAP = "invitedUserMap";
		}
	}
	
	public static class User {
		public static final String KIND = "User";
		
		public static class Property {
			public static final String GROUP_LIST = "groupList";
			public static final String COFFEES = "coffees";
			public static final String BASENAME = "basename";
			public static final String USERNAME = "username";
			public static final String PASSWORD = "password";
			public static final String INVITATION = "invitation";
		}
	}
	
	public static class Coffer {
		public static final String KIND = "Coffer";
		
		public static class Property {

			public static final String EXPIRATION_TIME = "expirationTime";
			public static final String INITIATOR = "initiator";
			public static final String OFFERER = "offerer";
			public static final String GROUP = "group";
			public static final String OFFEREDS = "offereds";
			public static final String STATUS = "status";
			
		}
	}
	
}
