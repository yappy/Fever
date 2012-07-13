package gamelib;

/**
 * Exception occured in this library.
 * @author yappy
 */
public class GameLibException extends Exception {

	private static final long serialVersionUID = 1L;

	public GameLibException() {
		super();
	}

	public GameLibException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameLibException(String message) {
		super(message);
	}

	public GameLibException(Throwable cause) {
		super(cause);
	}

}
