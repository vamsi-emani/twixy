package com.twixy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class AbstractTwixy implements OAuth, AutoAccess{

	private Scanner in = new Scanner(System.in);
	
	public Token getRequestToken(OAuthService service){
		// Obtain the Request Token
	    log("Fetching the Request Token");
	    Token requestToken = service.getRequestToken();
	    log("Got the Request Token!");
	    return requestToken;
	}
	
	public Verifier verifyUrl(Token requestToken, OAuthService service){
		log("Now go and authorize Scribe.");
		String authUrl = service.getAuthorizationUrl(requestToken);
	    Util.browse(authUrl);
	    log("Paste the verifier here");
	    System.out.print(">>");
	    Verifier verifier = new Verifier(getLineInput());	    
	    return verifier;
	}

	public Token getAccessToken(Token requestToken, Verifier verifier, OAuthService service){		
		// Trade the Request Token and Verfier for the Access Token
	    log("Trading the Request Token for an Access Token...");
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    log("Got the Access Token!");
	    /*log("(if you're curious, it looks like this: " + accessToken + " )");*/	    
	    return accessToken;
	}
	
	protected String getLineInput(){
		return in.nextLine();
	}
	
	public void log(Object msg){
		System.out.println("$ "+msg);
	}
	
	public void saveAccessToken(Token token){
		try {
			FileOutputStream fout = new FileOutputStream("temp.dat");
		    ObjectOutputStream oos = new ObjectOutputStream(fout);
		    oos.writeObject(token);
		    oos.close();
		}
		catch (Exception e) { 
			log("Unable to save access token");
		}
	}
	
	public Token reloadAccessToken(){
		Token token = null;
		try {
			FileInputStream fin = new FileInputStream("temp.dat");
		    ObjectInputStream ois = new ObjectInputStream(fin);
		     token = (Token) ois.readObject();		    
		    ois.close();		    
		}
		catch (IOException e) {
			log("Cannot load token/No access token found");			
		}catch (ClassNotFoundException e) {
			log("Cannot load token/No access token found");			
		}
		finally{
			return token;
		}		
	}

}
