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
import java.util.Properties;
import java.util.ResourceBundle;
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
public class TwixyConsole extends AbstractTwixyConsole{	 	
	
	private void login(){	
		Properties props = new Conf().load();		
		setService(new ServiceBuilder().provider(TwitterApi.class)				
				.apiKey(props.get("apiKey").toString())
				.apiSecret(props.get("apiSecret").toString())
				.build()
		);
	    log("=== Twitter's OAuth Workflow ===");	
	    setAccessToken(reloadAccessToken());
		if(!hasAccessToken()){			
			Token requestToken = getRequestToken(getService());
			Verifier verifier = verifyUrl(requestToken, getService());
			setAccessToken(getAccessToken(requestToken, verifier, getService()));
			saveAccessToken(getAccessToken());
		}
	}
	
	private void setUp(){
		this.setFriends(loadFriends());
		this.initConsoleCommands();
		this.printHelpInfo();
	}
	
	private void startApp(){
		this.login();		
		this.setUp();		
	    this.getTimeline(20);	    
	    this.talk();	 
	}
	
	public static void main(String[] args) {
		new TwixyConsole().startApp();
	}		
		
	public void initConsoleCommands(){
		addCommand("-u", new ICommand() {
			public Object exec(AbstractTwixyConsole console) {
				return console.getUserInfo(getUserInput());
			}
		})
		.addCommand("-n", new ICommand() {			
			public Object exec(AbstractTwixyConsole console) {						
				return console.getTimeline(Integer.parseInt(console.getUserInput()));
			}
		})
		.addCommand("-t", new ICommand() {			
			public Object exec(AbstractTwixyConsole console) {				
				console.tweet(console.getUserInput());
				return "Twitter status updated ...";
			}
		})
		.addCommand("-f", new ICommand() {		
			public Object exec(AbstractTwixyConsole console) {
				return console.listFriends(console.getUserInput());				
			}
		})
		.addCommand("help", new ICommand() {			
			public Object exec(AbstractTwixyConsole console) {
				console.printHelpInfo();
				return "";
			}
		}) ;
	}
	
	private void talk(){		
		while(!getUserInput().endsWith("!")){
			String line = getLineInput();
			if(line.length() > 2)
				setUserInput(line.substring(2));			
			if(line.equalsIgnoreCase("help"))
				printHelpInfo();			
			else{
				String actionType = line.substring(0, 2);
				log(executeCmd(actionType));				
			}
			
		}
	}
		
	public List<Friend> loadFriends() {		
		List<Friend> friends = new ArrayList();
	    OAuthRequest request = new OAuthRequest(Verb.GET, TwitterUrl.FRIENDS);
	    getService().signRequest(getAccessToken(), request);
	    Response response = request.send();
	    String jsonResponse = response.getBody();	   
	    JSONArray array = (JSONArray) new JSONObject(jsonResponse).get("users");	    
	    for(int i = 0 ; i<array.length(); i++){	   
	    	JSONObject json = (JSONObject) array.get(i);	    		    
	    	friends.add(new Friend(json.get("screen_name").toString()));
	    }	   
	    return friends;
	}

	
}
