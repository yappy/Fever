package gamelib.input;

import gamelib.GameLibException;

/**
 * Input exception.
 * @author yappy
 */
public class InputException extends GameLibException {

	private static final long serialVersionUID = 1L;

	public InputException() {
		super();
	}

	public InputException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputException(String message) {
		super(message);
	}

	public InputException(Throwable cause) {
		super(cause);
	}

}
