package com.twixy;

import org.scribe.model.Token;

/**
 * @author vamsi emani
 */
public interface AutoAccess {

	/**
	 * Implementation to persist accessToken 
	 */
	public void saveAccessToken(Token token);
	
	/**
	 * Implementation to reuse saved accessToken 
	 */
	public Token reloadAccessToken();
}
