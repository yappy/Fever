package gamelib.graphics;

import gamelib.GameLibException;

/**
 * @author yappy
 */
public class GraphicsException extends GameLibException {

	private static final long serialVersionUID = 1L;

	public GraphicsException() {
		super();
	}

	public GraphicsException(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphicsException(String message) {
		super(message);
	}

	public GraphicsException(Throwable cause) {
		super(cause);
	}

}
