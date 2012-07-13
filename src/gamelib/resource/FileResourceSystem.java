package gamelib.resource;

import gamelib.util.FileInputStreamEx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author yappy
 * 
 */
public class FileResourceSystem implements ResourceSystem {

	private File baseDir;

	public FileResourceSystem(File baseDir) {
		this.baseDir = baseDir;
	}

	public FileResourceSystem(String baseDirName) {
		this(new File(baseDirName));
	}

	@Override
	public InputStream getInputStream(String name) throws IOException {
		return new FileInputStreamEx(new File(baseDir, name));
	}

}
