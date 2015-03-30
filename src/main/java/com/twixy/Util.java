package com.twixy;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {

	public static void browse(String url){
		 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		 try {
	       desktop.browse(new URI(url));
	     } catch (IOException e) {
	    	 System.out.println(url);
		 } catch (URISyntaxException e) {
			 System.out.println(url);
		 }
	}

}
