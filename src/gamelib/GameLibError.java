package gamelib;

/**
 * Fatal error occured in this library.
 * @author yappy
 */
public class GameLibError extends Error {

	private static final long serialVersionUID = 1L;

	public GameLibError() {
		super();
	}

	public GameLibError(String message, Throwable cause) {
		super(message, cause);
	}

	public GameLibError(String message) {
		super(message);
	}

	public GameLibError(Throwable cause) {
		super(cause);
	}

}
