package gamelib.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yappy
 */
public final class ResourceManager {

	private ResourceSystem resourceSystem = new FileResourceSystem(".");

	public void setResourceSystem(ResourceSystem resourceSystem) {
		this.resourceSystem = resourceSystem;
	}

	public InputStream getInputStream(String name) throws IOException {
		return resourceSystem.getInputStream(name);
	}

	private ResourceManager() {}

	private static ResourceManager instance = new ResourceManager();

	public static ResourceManager getInstance() {
		return instance;
	}

}
