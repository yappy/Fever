package mj.game;

import gamelib.Game;
import gamelib.GameException;
import gamelib.GameLibException;
import gamelib.Scene;
import gamelib.input.InputManager;
import gamelib.util.Trace;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Collections;

import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Keyboard;

/**
 * @author yappy
 * 
 */
public class GameMain extends Game {

	private static final String TITLE = "mjkt";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int FPS = 30;

	private static final Keyboard keyboard = InputManager.GetInstance()
			.getKeyboard();
	private static final Font FPS_FONT = new Font(Font.MONOSPACED, 0, 12);

	public GameMain() {
		super(TITLE, Collections.<Image> emptyList(), WIDTH, HEIGHT, FPS);
	}

	@Override
	protected Scene initialize() throws GameLibException, GameException {
		return new GameScene(System.currentTimeMillis());
	}

	@Override
	protected void terminate() throws GameLibException, GameException {
	}

	@Override
	protected void frame() throws GameLibException, GameException {
		keyboard.poll();
		boolean alt = keyboard.isKeyDown(Key.LALT)
				|| keyboard.isKeyDown(Key.RALT);
		boolean enter = keyboard.isKeyDown(Key.RETURN);
		if (alt && enter) {
			toggleScreenMode();
		}
	}

	@Override
	protected void renderFinish(Graphics2D g) throws GameLibException,
			GameException {
		g.setColor(Color.BLACK);
		g.fillRect(WIDTH - 120, HEIGHT - 15, 120, 15);
		g.setColor(Color.WHITE);
		g.setFont(FPS_FONT);
		g.drawString(frameSync.toString(), WIDTH - 120, HEIGHT - 5);
	}

	public static void main(String[] args) {
		Trace.setDebug(true);
		GameMain main = new GameMain();
		main.startGame(false);
	}

}
