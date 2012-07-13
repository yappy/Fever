package gamelib.input;

import java.util.ArrayList;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;

/**
 * @author yappy
 */
public final class InputManager {

	private static final ControllerEnvironment env = ControllerEnvironment
			.getDefaultEnvironment();
	private Controller[] cons;

	public void updateControllers() {
		cons = env.getControllers();
	}

	public void pollAll() {
		for (Controller con : cons) {
			con.poll();
		}
	}

	public Keyboard getKeyboard() {
		int max = 0;
		Keyboard result = null;
		for (Controller con : cons) {
			if (con.getType() == Type.KEYBOARD) {
				if (con.getComponents().length > max) {
					max = con.getComponents().length;
					result = (Keyboard) con;
				}
			}
		}
		if (result == null)
			throw new InputError("Keyboard not found.");
		return result;
	}

	/**
	 * Return a pressed component index.
	 * 
	 * @param con
	 * @return -1 if no components were pressed
	 */
	public int getAnyPressedIndex(Controller con) {
		Component[] coms = con.getComponents();
		for (int i = 0; i < coms.length; i++) {
			if (Math.abs(coms[i].getPollData()) > 0.5f)
				return i;
		}
		return -1;
	}

	public int getKeyIndex(Key key) {
		Component[] cs = getKeyboard().getComponents();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i].getIdentifier() == key)
				return i;
		}
		throw new InputError("Key " + key + " not found");
	}

	public Controller[] getGamePads() {
		List<Controller> res = new ArrayList<Controller>();
		for (Controller con : cons) {
			Type type = con.getType();
			if (type != Type.KEYBOARD && type != Type.MOUSE) {
				res.add(con);
			}
		}
		return res.toArray(new Controller[0]);
	}

	private static InputManager instance = new InputManager();

	private InputManager() {
		updateControllers();
	}

	public static InputManager GetInstance() {
		return instance;
	}

}
