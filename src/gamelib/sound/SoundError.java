package gamelib.sound;

import gamelib.GameLibError;

/**
 * Fatal sound error.
 * @author yappy
 */
public class SoundError extends GameLibError {

	private static final long serialVersionUID = 1L;

	public SoundError() {
		super();
	}

	public SoundError(String message, Throwable cause) {
		super(message, cause);
	}

	public SoundError(String message) {
		super(message);
	}

	public SoundError(Throwable cause) {
		super(cause);
	}

}
