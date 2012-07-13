package gamelib.graphics;

import gamelib.GameLibError;

/**
 * @author yappy
 * 
 */
public class GraphicsError extends GameLibError {

	private static final long serialVersionUID = 1L;

	public GraphicsError() {
		super();
	}

	public GraphicsError(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphicsError(String message) {
		super(message);
	}

	public GraphicsError(Throwable cause) {
		super(cause);
	}

}
