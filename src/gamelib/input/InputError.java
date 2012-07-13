package gamelib.input;

import gamelib.GameLibError;

/**
 * Fatal input error.
 * @author yappy
 */
public class InputError extends GameLibError {

	private static final long serialVersionUID = 1L;

	public InputError() {
		super();
	}

	public InputError(String message, Throwable cause) {
		super(message, cause);
	}

	public InputError(String message) {
		super(message);
	}

	public InputError(Throwable cause) {
		super(cause);
	}

}
