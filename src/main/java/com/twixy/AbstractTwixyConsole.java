package com.twixy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class AbstractTwixyConsole extends AbstractTwixy implements Twixy{	
	
	private OAuthService service ;
	private List<Friend> friends;
	private String userInput="";
	private HashMap<String, ICommand> commandMap = new HashMap<String, ICommand>();
	private Token accessToken;
	
	protected AbstractTwixyConsole addCommand(String actionType, ICommand cmd){
		commandMap.put(actionType, cmd);
		return this;
	}
	
	protected Object executeCmd(String actionType){
		ICommand cmd = commandMap.get(actionType);
		if(cmd == null)
			return cmd;
		return cmd.exec(this);
	}
	
	public String getUserInput() {
		return userInput.trim(); 
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	protected OAuthService getService() {
		return service;
	}

	protected void setService(OAuthService service) {
		this.service = service;
	}

	protected boolean hasAccessToken(){
		return accessToken != null;
	}
	
	protected void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}
		
	protected Token getAccessToken(){		
		return accessToken;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}	
	
	public List<String> getTimeline(int count){
		List timeline = new ArrayList(count);
	    OAuthRequest request = new OAuthRequest(Verb.GET, TwitterUrl.TIMELINE+"?count="+count);	    	   
	    JSONArray array = new JSONArray(getResponse(request));	    
	    log("=== HOME ===");	    
	    for(int i = 0 ; i<array.length(); i++){	   
	    	JSONObject json = (JSONObject) array.get(i);
	    	String user = ((JSONObject) json.get("user")).get("screen_name").toString();
	    	String tweet = "@"+user + " \n     " + json.get("text");
	    	log(tweet);
	    	timeline.add(tweet);
	    }	    
	    return timeline;
	}
	
	public void tweet(String status){
		if(status.length() < 140){			
		    OAuthRequest request = new OAuthRequest(Verb.POST, TwitterUrl.STATUS_UPDATE);
		    request.addBodyParameter("status", status);
		    service.signRequest(accessToken, request);
		    Response response = request.send();		    
		}else
			log("char limit exceeded 140");
	}
	
	public List<Friend> getFriends() {		
		return friends;
	}
	
	public List<Friend> listFriends(String partialName){
		List<Friend> list = new ArrayList<Friend>();
		for(Friend friend : getFriends()){
			log(friend);
			/*if(friend.getScreenName().toLowerCase().contains(partialName.toLowerCase())){
				
				list.add(friend);
			}*/
		}
		return list;
	}
	
	public TwitterUser getUserInfo(String screenName){
		OAuthRequest request = new OAuthRequest(Verb.GET, TwitterUrl.USER_INFO+"?screen_name="+screenName);	    	   
		JSONObject user = new JSONObject(getResponse(request));	    	    
	    TwitterUser twitterUser = new TwitterUser();
	    twitterUser.setScreenName(user.get("screen_name").toString());
	    twitterUser.setDescription(user.get("description").toString());
	    twitterUser.setProfileImageUrl(user.get("profile_image_url_https").toString());	    
//	    twitterUser.setFavortiesCount(user.get("favorite_count").toString());
	    twitterUser.setFollowersCount(user.get("followers_count").toString());
	    return twitterUser;
	}
	
	private String getResponse(OAuthRequest request){
		getService().signRequest(accessToken, request);
	    Response response = request.send();
	    String jsonResponse = response.getBody();
	    return jsonResponse;
	}
	
	public void printHelpInfo(){
		log("TO TWEET, HYPHEN t FOLLOWED BY YOUR TWEET AND HIT ENTER\n\tUsage : -t Hello world from twixy console!.");
		log("TO GET LAST N TWEETS, HYPEHN n FOLLOWED BY NUMBER.\n\tUsage : -n 10");
		log("TO VIEW FRIENDS, HYPHEN f !\n\tUsage : -f");
		log("TO VIEW USER PROFILE, HYPHEN u FOLLOED BY TWITTER HANDLE !\n\tUsage : -u @narendramodi");
		log("TO QUIT ENTER !");
	}
}
