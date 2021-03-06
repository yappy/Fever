package gamelib.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This class add useful methods to {@link Properties}.<br>
 * If the key is not exist, return defaultValue and set property.
 * @author yappy
 */
public class PropertiesEx {

	private Properties prop;

	public PropertiesEx() {
		prop = new Properties();
	}

	public String getString(String key, String defaultValue) {
		String val = prop.getProperty(key);
		if(val == null){
			setString(key, defaultValue);
			return defaultValue;
		}
		return val;
	}

	public void setString(String key, String value) {
		prop.setProperty(key, value);
	}

	public int getInt(String key, int defaultValue) {
		try{
			return Integer.parseInt(prop.getProperty(key));
		}
		catch(Exception e){
			setInt(key, defaultValue);
			return defaultValue;
		}
	}

	public void setInt(String key, int value) {
		setString(key, String.valueOf(value));
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		try{
			return prop.getProperty(key).equals("true");
		}
		catch(Exception e){
			setBoolean(key, defaultValue);
			return defaultValue;
		}
	}

	public void setBoolean(String key, boolean value) {
		setString(key, String.valueOf(value));
	}

	public void loadFromXML(InputStream in) throws IOException {
		prop.loadFromXML(in);
	}

	public void storeToXML(OutputStream out, String comment) throws IOException {
		prop.storeToXML(out, comment);
	}

}
