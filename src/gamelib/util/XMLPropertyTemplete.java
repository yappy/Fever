package gamelib.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author yappy
 */
public final class XMLPropertyTemplete {

	public static void create(File file) throws IOException {
		Properties prop = new Properties();
		prop.setProperty("sample.key", "value");
		prop.setProperty("sample.key2", "a\nb");
		prop.storeToXML(new FileOutputStream(file), "Generated empty file");
	}

	public static void main(String[] args) throws IOException {
		create(new File("empty.xml"));
	}

}
