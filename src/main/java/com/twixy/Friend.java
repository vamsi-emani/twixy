package com.twixy;

public class Friend {

	private String screenName;
	
	public Friend(String screenName) {
		this.screenName = screenName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	@Override
	public String toString() {	
		return "@"+screenName;
	}
}
