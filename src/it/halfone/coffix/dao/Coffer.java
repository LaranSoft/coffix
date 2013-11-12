package it.halfone.coffix.dao;


/**
 * @author a.larosa
 */
public class Coffer {

	private String creator;
	private String offerer;
	private String offereds;
	private long expireTime;
	private long state;
	private String key;

	public Coffer() {
	}

	public String getCreator() {
		return creator;
	}

	public void setInitiator(String creator) {
		this.creator = creator;
	}

	public String getOfferer() {
		return offerer;
	}

	public void setOfferer(String offererJson) {
		this.offerer = offererJson;
	}

	public String getOffereds() {
		return offereds;
	}

	public void setOffereds(String offereds) {
		this.offereds = offereds;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long remainingTime) {
		this.expireTime = remainingTime;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
