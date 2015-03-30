package com.twixy;

/**
 * 
 * @author vamsi emani
 *
 */

public class TwitterUser {
	
	private String screenName, description, profileImageUrl, followersCount, favoriteCount; 
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screeName) {
		this.screenName = screeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public String toString() {	
		return "USER : " + "@"+ getScreenName() + 
				"\n\t" + getDescription()+"\nProfile Image : "+getProfileImageUrl()+
				"\nFollowers count : "+ getFollowersCount();
				/*"\nFavorites : "+ getFavortiesCount()*/
	}

	public String getFavortiesCount() {
		return favoriteCount;
	}

	public void setFollowersCount(String count) {		
		this.followersCount = count;
	}
	
	public void setFavortiesCount(String count) {
		this.favoriteCount = count;
	}

	public String getFollowersCount() {		
		return followersCount;
	}
}
