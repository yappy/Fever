package gamelib.sound;

import gamelib.GameLibException;

/**
 * Sound exception.
 * @author yappy
 */
public class SoundException extends GameLibException {

	private static final long serialVersionUID = 1L;

	public SoundException() {
		super();
	}

	public SoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SoundException(String message) {
		super(message);
	}

	public SoundException(Throwable cause) {
		super(cause);
	}

}
