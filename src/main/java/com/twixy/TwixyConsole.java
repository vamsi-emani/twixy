package com.twixy;
import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
/**
 * @author Vamsi Emani
 */
public class TwixyConsole extends AbstractTwixy implements Twixy{	 	
	
	private OAuthService service;
	private Token accessToken;
	
	private void startApp(){
		service = new ServiceBuilder()
        .provider(TwitterApi.class)
        .apiKey("TROnC8SdELv8RggkYyn86w")
        .apiSecret("YDsRrPeYVCw42jPtBLurwtOKnPgk51Z7gg4Eatsw")
        .build();
	    log("=== Twitter's OAuth Workflow ===");	
	    accessToken = reloadAccessToken();
		if(accessToken == null){			
			Token requestToken = getRequestToken(service);
			Verifier verifier = verifyUrl(requestToken, service);
			accessToken = getAccessToken(requestToken, verifier, service);
			saveAccessToken(accessToken);
		}	    
	    getTimeline(20);	    
	    talk();	 
	}
	
	public static void main(String[] args) {
		new TwixyConsole().startApp();
	}		
	
	public List<String> getTimeline(int count){
		List timeline = new ArrayList(count);
	    OAuthRequest request = new OAuthRequest(Verb.GET, TwitterUrl.TIMELINE+"?count="+count);
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    String jsonResponse = response.getBody();	   
	    JSONArray array = new JSONArray(jsonResponse);
	    log("----------------");
	    log("HOME");
	    log("----------------");
	    for(int i = 0 ; i<array.length(); i++){	   
	    	JSONObject json = (JSONObject) array.get(i);
	    	String user = ((JSONObject) json.get("user")).get("screen_name").toString();
	    	String tweet = user + " | " + json.get("text");
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
	
	private void talk(){
		String userInput = "";
		while(!userInput.endsWith("!")){
			userInput = getLineInput();
			if(userInput.equalsIgnoreCase("help"))
				printHelpInfo();			
			else if(userInput.length() != 0){
				char actionType = userInput.charAt(0);			
				switch (actionType) {								
				case '-':					
					getTimeline(Integer.parseInt(userInput.substring(1)));
				default:
					if(!userInput.toLowerCase().startsWith("help") || userInput.endsWith("!"))
						tweet(userInput);						
				}
			}
			
		}
	}
		
	public void printHelpInfo(){
		log("ENTER TEXT & HIT ENTER TO UPDATE STATUS.");
		log("TO GET LAST N TWEETS, ENTER - FOLLOWED BY NUMBER.\n\tUsage : -10");
		log("TO QUIT ENTER !");
	}
}
