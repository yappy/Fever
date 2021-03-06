package gamelib.input;

import gamelib.util.PropertiesEx;

import java.awt.Frame;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.Component.Identifier.Key;

/**
 * @author yappy
 */
public class InputConfiguration {

	private InputManager inputManager = InputManager.GetInstance();
	private Controller[] gamepads = inputManager.getGamePads();

	private final int playerCount;
	private final int keyCount;
	private final String[] keyDescs;

	private boolean[] isPad;
	private int[][] keys;
	private int[][] padButtons;

	/**
	 * @param playerCount
	 *            the number of players
	 * @param keyDescs
	 *            descriptions of keys(length=keyCount)
	 */
	public InputConfiguration(int playerCount, String[] keyDescs) {
		this.playerCount = playerCount;
		this.keyDescs = Arrays.copyOf(keyDescs, keyDescs.length);
		this.keyCount = keyDescs.length;
		if (keyCount < 4 || keyCount > PAD_BUTTONS.size())
			throw new InvalidParameterException("Invalid keyCount");
		isPad = new boolean[playerCount];
		keys = new int[playerCount][keyCount];
		padButtons = new int[playerCount][keyCount];
		setDefaults();
	}

	public InputDevice[] createInputDevices() {
		InputDevice[] res = new InputDevice[playerCount];
		for (int i = 0; i < playerCount; i++) {
			res[i] = createInputDevice(i);
		}
		return res;
	}

	public InputDevice createInputDevice(int no) {
		if (isPad[no]) {
			return InputDevice.createInputDevice(
					inputManager.getGamePads()[no], padButtons[no]);
		} else {
			return InputDevice.createInputDevice(inputManager.getKeyboard(),
					keys[no]);
		}
	}

	public void showConfigDialog(Frame owner, String title) {
		InputConfigDialog dialog = new InputConfigDialog(owner, title, this);
		dialog.setVisible(true);
	}

	public void load(PropertiesEx prop) {
		for (int p = 0; p < playerCount; p++) {
			isPad[p] = prop.getBoolean("input.type." + p, true);
			for (int k = 0; k < keyCount; k++) {
				keys[p][k] = prop.getInt("input.key." + p + "." + k, -1);
				padButtons[p][k] = prop.getInt("input.pad." + p + "." + k, -1);
			}
		}
		autoModify();
		store(prop);
	}

	public void store(PropertiesEx prop) {
		for (int p = 0; p < playerCount; p++) {
			prop.setBoolean("input.type." + p, isPad[p]);
			for (int k = 0; k < keyCount; k++) {
				prop.setInt("input.key." + p + "." + k, keys[p][k]);
				prop.setInt("input.pad." + p + "." + k, padButtons[p][k]);
			}
		}
	}

	public void setDefaults() {
		PropertiesEx prop = new PropertiesEx();
		load(prop);
	}

	/**
	 * Return a copy of this configuration.<br>
	 * This method uses {@link #load(PropertiesEx)} and
	 * {@link #store(PropertiesEx)}.<br>
	 * Don't call this frequently.
	 * 
	 * @return copy
	 */
	public InputConfiguration createCopy() {
		PropertiesEx prop = new PropertiesEx();
		store(prop);
		InputConfiguration copy = new InputConfiguration(playerCount, keyDescs);
		copy.load(prop);
		return copy;
	}

	/**
	 * Copy configration data from src.<br>
	 * This method uses {@link #load(PropertiesEx)} and
	 * {@link #store(PropertiesEx)}.<br>
	 * Don't call this frequently.
	 * 
	 * @param src
	 *            copy source
	 */
	public void copyFrom(InputConfiguration src) {
		PropertiesEx prop = new PropertiesEx();
		src.store(prop);
		this.load(prop);
		autoModify();
	}

	private void autoModify() {
		// auto pad off if enough pads were not found
		int padNum = gamepads.length;
		for (int p = 0; p < playerCount; p++) {
			if (p >= padNum) {
				isPad[p] = false;
			}
		}
		// auto disable if the button is not exist.
		for (int p = 0; p < playerCount; p++) {
			if (!isPad[p])
				continue;
			int numButtons = gamepads[p].getComponents().length;
			for (int k = 0; k < keyCount; k++) {
				if (keys[p][k] >= numButtons) {
					keys[p][k] = -1;
				}
			}
		}
		// some 1P keys auto set
		final Key[] DEFAULT_KEY = { Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT,
				Key.Z, Key.X, Key.C, Key.V, Key.A, Key.S, Key.D, Key.F };
		for (int k = 0; k < keys[0].length && k < DEFAULT_KEY.length; k++) {
			keys[0][k] = (keys[0][k] != -1) ? keys[0][k] : inputManager
					.getKeyIndex(DEFAULT_KEY[k]);
		}
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public int getKeyCount() {
		return keyCount;
	}

	public String[] getKeyDescs() {
		return keyDescs;
	}

	public boolean[] getIsPad() {
		return isPad;
	}

	public int[][] getKeys() {
		return keys;
	}

	public int[][] getPadButtons() {
		return padButtons;
	}

	private static final List<Button> PAD_BUTTONS;
	static {
		final Button[] SRC = { Button._0, Button._1, Button._2, Button._3,
				Button._4, Button._5, Button._6, Button._7, Button._8,
				Button._9, Button._10, Button._11, Button._12, Button._13,
				Button._14, Button._15, Button._16, Button._17, Button._18,
				Button._19, };
		PAD_BUTTONS = Collections.unmodifiableList(Arrays.asList(SRC));
	}

	public static void main(String[] args) {
		InputConfiguration config = new InputConfiguration(2, new String[] {
				"UP", "DOWN", "LEFT", "RIGHT", "A", "B", "X", "Y" });
		// config.isPad[0] = false;
		config.showConfigDialog(null, "Key Config");
		/*
		 * final InputDevice dev = config.createInputDevice(0); JFrame frame =
		 * new JFrame("test"); frame.setSize(640, 480);
		 * frame.setLocationRelativeTo(null);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); final JTextArea
		 * text = new JTextArea(); text.setEditable(false); frame.add(text); new
		 * Timer(100, new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { dev.poll();
		 * text.setText(""); for(int i = 0; i < 8; i++){ text.append("i: " +
		 * dev.isDown(i) + "\n"); } } }).start(); frame.setVisible(true);
		 */
	}

}
