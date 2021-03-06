package gamelib.input;

import java.util.Arrays;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller.Type;

/**
 * ID 0,1,2,3 are directional input.<br>
 * The others are button input.
 * 
 * @author yappy
 */
public abstract class InputDevice {

	public static final int ID_UP = 0;
	public static final int ID_DOWN = 1;
	public static final int ID_LEFT = 2;
	public static final int ID_RIGHT = 3;

	public static InputDevice createInputDevice(Controller con, int[] keyMap) {
		if (con.getType() == Type.KEYBOARD) {
			return new KeyboradDevice(con, keyMap);
		} else {
			return new GamepadDevice(con, keyMap);
		}
	}

	private Controller con;
	private Component[] buttons;
	private int[] count;

	private InputDevice(Controller con, int[] keyMap) {
		this.con = con;
		buttons = new Component[keyMap.length];
		count = new int[keyMap.length];
		Component[] coms = con.getComponents();
		for (int i = 0; i < keyMap.length; i++) {
			if (keyMap[i] == -1 || keyMap[i] >= coms.length)
				buttons[i] = null;
			else
				buttons[i] = coms[keyMap[i]];
		}
	}

	public void clearOldInput() {
		Arrays.fill(count, 0);
	}

	public void poll() {
		con.poll();
		for (int i = 0; i < buttons.length; i++) {
			if (getButtonState(i)) {
				count[i]++;
			} else {
				count[i] = 0;
			}
		}
	}

	protected boolean getButtonState(int id) {
		if (buttons[id] == null)
			return false;
		return buttons[id].getPollData() > 0.5f;
	}

	public boolean isDown(int id) {
		return count[id] != 0;
	}

	public boolean isDownAny() {
		for (int i = 0; i < buttons.length; i++) {
			if (isDown(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDownFirst(int id) {
		return count[id] == 1;
	}

	public boolean isDownFirstAny() {
		for (int i = 0; i < buttons.length; i++) {
			if (isDownFirst(i)) {
				return true;
			}
		}
		return false;
	}

	private static class KeyboradDevice extends InputDevice {
		private KeyboradDevice(Controller con, int[] keyMap) {
			super(con, keyMap);
		}
	}

	private static class GamepadDevice extends InputDevice {
		private Component axisx, axisy;

		private GamepadDevice(Controller con, int[] keyMap) {
			super(con, keyMap);
			axisx = con.getComponent(Identifier.Axis.X);
			axisy = con.getComponent(Identifier.Axis.Y);
		}

		@Override
		protected boolean getButtonState(int id) {
			if (axisx == null || axisy == null)
				return false;
			switch (id) {
			case ID_UP:
				return axisy.getPollData() < -0.5f;
			case ID_DOWN:
				return axisy.getPollData() > 0.5f;
			case ID_LEFT:
				return axisx.getPollData() < -0.5f;
			case ID_RIGHT:
				return axisx.getPollData() > 0.5f;
			default:
				return super.getButtonState(id);
			}
		}
	}

}
