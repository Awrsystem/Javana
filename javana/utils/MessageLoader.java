package br.com.javana.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MessageLoader {
	private Properties props = new Properties();
	private static MessageLoader instance = new MessageLoader();
	
	public static MessageLoader getInstance(){
		return instance;
	}
	
	public String getMessage(Integer number, String... params){
		try {
			props.load(new FileInputStream("conf/message.properties"));
			String loaded = props.getProperty(number.toString());
			if(params.length > 0){
				String[] s = loaded.split("&\\d*");
				String r = s[0] + params[0] + s[1];
				return r;
			}
			else{
				return loaded;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
