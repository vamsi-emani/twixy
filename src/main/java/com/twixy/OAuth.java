package com.twixy;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author vamsi emani
 */

public interface OAuth {

	public Token getRequestToken(OAuthService service);
	
	public Verifier verifyUrl(Token requestToken, OAuthService service);

	public Token getAccessToken(Token requestToken, Verifier verifier, OAuthService service);
	
}
