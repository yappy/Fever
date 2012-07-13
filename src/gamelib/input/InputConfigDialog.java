package gamelib.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;

/**
 * Input config.<br>
 * You can let users choose input keys.
 * @author yappy
 */
public class InputConfigDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final InputManager inputManager = InputManager.GetInstance();
	private final Keyboard keyboard = inputManager.getKeyboard();
	private Controller[] gamepads = inputManager.getGamePads();
	private Timer timer;
	private static final int TIMER_INTERVAL = 100;

	private int playerCount;
	private int keyCount;
	private String[] keyDescs;
	private InputConfiguration targetConfig;
	private InputConfiguration workConfig;
	private boolean[] isPad;
	private int[][] keys;
	private int[][] padButtons;

	private static final Color COLOR_NOT_SELECTED = Color.WHITE;
	private static final Color COLOR_KEY_SELECTED = new Color(255, 255, 128);
	private static final Color COLOR_PAD_SELECTED = new Color(255, 128, 128);
	private JTabbedPane tabbedPane;
	private JRadioButton[] keyRadio;
	private JRadioButton[] padRadio;
	private int[] keyIndex;
	private JLabel[][] keyLabels;
	private int[] padIndex;
	private JLabel[][] padLabels;

	public InputConfigDialog(Frame owner, String title, InputConfiguration config) {
		super(owner, title, true);

		this.playerCount = config.getPlayerCount();
		this.keyCount = config.getKeyCount();
		this.keyDescs = config.getKeyDescs();
		targetConfig = config;
		workConfig = targetConfig.createCopy();
		this.isPad = workConfig.getIsPad();
		this.keys = workConfig.getKeys();
		this.padButtons = workConfig.getPadButtons();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(640, 480);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				timer.start();
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				timer.stop();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				actionCancel();
			}
		});

		keyRadio = new JRadioButton[playerCount];
		padRadio = new JRadioButton[playerCount];
		keyIndex = new int[keyCount];
		padIndex = new int[keyCount];
		Arrays.fill(padIndex, 4);
		keyLabels = new JLabel[playerCount][keyCount];
		padLabels = new JLabel[playerCount][keyCount];
		tabbedPane = new JTabbedPane();
		final ActionListener radioAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionRadio();
			}
		};
		for(int p = 0; p < playerCount; p++){
			JPanel panel = new JPanel(new GridLayout(keyCount + 1, 3));
			panel.add(new JPanel());
			boolean padEnabled = p < gamepads.length;
			ButtonGroup buttonGroup = new ButtonGroup();
			keyRadio[p] = new JRadioButton("Keyboard");
			keyRadio[p].addActionListener(radioAction);
			padRadio[p] = new JRadioButton(padEnabled ? gamepads[p].getName() : "No Gamepad");
			padRadio[p].setEnabled(padEnabled);
			padRadio[p].addActionListener(radioAction);
			buttonGroup.add(keyRadio[p]);
			buttonGroup.add(padRadio[p]);
			panel.add(keyRadio[p]);
			panel.add(padRadio[p]);
			for(int k = 0; k < keyCount; k++){
				panel.add(new JLabel(keyDescs[k], JLabel.CENTER));
				keyLabels[p][k] = new JLabel("", JLabel.CENTER);
				keyLabels[p][k].setOpaque(true);
				panel.add(keyLabels[p][k]);
				padLabels[p][k] = new JLabel("", JLabel.CENTER);
				padLabels[p][k].setOpaque(true);
				panel.add(padLabels[p][k]);
			}
			tabbedPane.addTab((p + 1) + "P", panel);
		}
		add(tabbedPane, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new FlowLayout());
		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOK();
			}
		});
		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionCancel();
			}
		});
		JButton buttonDefault = new JButton("Default");
		buttonDefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionDefault();
			}
		});
		bottom.add(buttonOK);
		bottom.add(buttonCancel);
		bottom.add(buttonDefault);
		add(bottom, BorderLayout.SOUTH);

		disableAllFocus(this);
		updateComponents();

		timer = new Timer(TIMER_INTERVAL, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processTimer();
			}
		});
	}

	private void disableAllFocus(Container container) {
		for(java.awt.Component comp : container.getComponents()){
			comp.setFocusable(false);
			if(comp instanceof Container){
				disableAllFocus((Container)comp);
			}
		}
	}

	private void updateComponents() {
		Component[] keyComps = keyboard.getComponents();
		for(int p = 0; p < playerCount; p++){
			if(isPad[p]){
				if(!padRadio[p].isEnabled())
					throw new InputError("Invalid state");
				padRadio[p].setSelected(true);
			}
			else{
				keyRadio[p].setSelected(true);
			}
			for(int k = 0; k < keyCount; k++){
				if(keys[p][k] != -1)
					keyLabels[p][k].setText(keyComps[keys[p][k]].toString());
				else
					keyLabels[p][k].setText("Disabled");
				keyLabels[p][k].setBackground(keyIndex[p] == k ? COLOR_KEY_SELECTED : COLOR_NOT_SELECTED);
				if(p < gamepads.length){
					Component[] padComps = gamepads[p].getComponents();
					if(padButtons[p][k] != -1)
						padLabels[p][k].setText(padComps[padButtons[p][k]].getIdentifier().getName());
					else
						padLabels[p][k].setText(k < 4 ? "-" : "Disabled");
					padLabels[p][k].setBackground(padIndex[p] == k ? COLOR_PAD_SELECTED : COLOR_NOT_SELECTED);
				}
			}
		}
	}

	private void actionRadio() {
		for(int p = 0; p < playerCount; p++){
			isPad[p] = padRadio[p].isSelected();
		}
	}

	private int prevKey = -1;
	private int prevPad = -1;

	private void processTimer() {
		boolean update = false;
		int playerNo = tabbedPane.getSelectedIndex();
		inputManager.pollAll();
		// keyboard any hit
		int key = inputManager.getAnyPressedIndex(keyboard);
		if(key != -1 && key != prevKey){
			keys[playerNo][keyIndex[playerNo]] = key;
			keyIndex[playerNo]++;
			keyIndex[playerNo] %= keyCount;
			isPad[playerNo] = false;
			update = true;
		}
		prevKey = key;
		// pad any hit
		if(playerNo < gamepads.length){
			int button = inputManager.getAnyPressedIndex(gamepads[playerNo]);
			if(button != -1 && button != prevPad){
				padButtons[playerNo][padIndex[playerNo]] = button;
				padIndex[playerNo]++;
				padIndex[playerNo] %= keyCount;
				padIndex[playerNo] = padIndex[playerNo] < 4 ? 4 : padIndex[playerNo];
				isPad[playerNo] = true;
				update = true;
			}
			prevPad = button;
		}
		if(update){
			updateComponents();
		}
	}

	private void actionOK() {
		targetConfig.copyFrom(workConfig);
		dispose();
	}

	private void actionCancel() {
		dispose();
	}

	private void actionDefault() {
		workConfig.setDefaults();
		updateComponents();
	}

}
