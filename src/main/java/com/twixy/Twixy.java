package com.twixy;

import java.util.List;

/**
 * @author vamsi emani
 */
public interface Twixy {

	public List<String> getTimeline(int count);
	
	public void tweet(String status);
	
	public List<Friend> getFriends();
}
