package it.halfone.coffix.dao;

import java.io.Serializable;

public class TwoStepInfo implements Serializable{

	private static final long serialVersionUID = 3861695689395655042L;
	
	private String target;
	private Context context;
	
	public TwoStepInfo() {}
	
	public TwoStepInfo(String target, Context context) {
		this.target = target;
		this.context = context;
	}

	public String getTarget() {
		return target;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
