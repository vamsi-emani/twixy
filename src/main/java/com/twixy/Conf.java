package com.twixy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Conf {

	private Properties props;
	
	public Properties load(){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		this.props = new Properties();
		try {
			InputStream resourceStream = loader.getResourceAsStream("conf.properties");
		    props.load(resourceStream);
		} catch (IOException e) {			
			System.out.println("Unable to load config.properties file");
		}
		return props;
	}
}