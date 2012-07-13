package gamelib.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yappy
 */
public interface ResourceSystem {

	InputStream getInputStream(String name) throws IOException;

}
