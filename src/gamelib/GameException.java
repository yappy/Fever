package gamelib;

/**
 * Exception occured in user code.
 * @author yappy
 */
public class GameException extends Exception {

	private static final long serialVersionUID = 1L;

	public GameException() {
		super();
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}

}
