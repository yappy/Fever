package mj.game;

import ec.util.MersenneTwister;
import gamelib.GameException;
import gamelib.GameLibException;
import gamelib.PrimaryScene;
import gamelib.SceneController;
import gamelib.graphics.SpriteSet;
import gamelib.input.InputConfiguration;
import gamelib.input.InputDevice;
import gamelib.sound.SoundSet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import mj.algo.Hai;

/**
 * @author yappy
 */
public class GameScene extends PrimaryScene {

	/* MJ Game Data */
	private MersenneTwister rand;
	private Queue<Integer> yama = new ArrayDeque<>();
	private List<List<Integer>> tehai = new ArrayList<>();
	// ID sequence
	private List<Integer> turnMap = new ArrayList<>();
	// ID (0..2|3)
	private int myID = 0;
	// index of myID in turnMap
	private int myIndex = -1;
	// whose turn (turnMap index)
	private int turnIndex = 0;

	/* Action Stream */
	private static final int QUEUE_SIZE = 16;
	private BlockingQueue<MJAction> actionQueue = new ArrayBlockingQueue<>(
			QUEUE_SIZE, true);

	/* Device */
	// temp
	private static InputDevice input = new InputConfiguration(1, new String[] {
			"", "", "", "", "OK", "Cancel" }).createInputDevice(0);

	private static final int HAI_W = 33;
	private static final int HAI_H = 59;

	// TODO State enum
	private static enum MJState {
		PLAY, KAN, PON_CHI
	}

	public GameScene(long randSeed) {
		super("Game Scene", new LoadingRendererImpl());
		initGame(randSeed);
	}

	private void initGame(long randSeed) {
		rand = new MersenneTwister(randSeed);

		// seat shuffle
		for (int i = 0; i < 3; i++) {
			turnMap.add(i);
		}
		Collections.shuffle(turnMap, rand);
		// find my seat index
		for (int i = 0; i < turnMap.size(); i++) {
			if (turnMap.get(i) == myID) {
				myIndex = i;
			}
		}
		assert myIndex >= 0 && myIndex < turnMap.size();

		// yama shuffle
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 136; i++) {
			int hai = i / 4;
			// sanma
			if (Hai.isManzu(hai) && Hai.isChunchan(hai))
				continue;
			list.add(i);
		}
		Collections.shuffle(list, rand);
		yama = new ArrayDeque<>(list);

		// haipai
		for (int i = 0; i < 3; i++) {
			List<Integer> hand = new ArrayList<>();
			for (int t = 0; t < 14; t++) {
				hand.add(yama.poll());
			}
			Collections.sort(hand);
			tehai.add(hand);
		}
	}

	@Override
	public void doFrame(SceneController sceneController)
			throws GameLibException, GameException {
		input.poll();
		if (input.isDownFirst(InputDevice.ID_LEFT)) {
			myID--;
		} else if (input.isDownFirst(InputDevice.ID_RIGHT)) {
			myID++;
		}
		myID = (myID + 4) % 4;
		for (int i = 0; i < turnMap.size(); i++) {
			if (turnMap.get(i) == myID) {
				myIndex = i;
			}
		}
	}

	@Override
	public void render(Graphics2D g) throws GameLibException, GameException {
		// clear
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 800, 600);
		// draw tehai
		for (int i = 0; i < turnMap.size(); i++) {
			int pos = ((i - myIndex) + 4) % 4;
			assert pos >= 0 && pos < 4;
			drawTehai(g, turnMap.get(i), pos);
		}
		// for (int i = 0; i < 14; i++) {
		// g.drawImage(getImage(i), HAI_W * i, 100, null);
		// }
	}

	private void drawTehai(Graphics2D g, int id, int pos) {
		// TODO drawTehai
		List<Integer> list = tehai.get(id);
		switch (pos) {
		// myself
		case 0:
			for (int i = 0; i < list.size(); i++) {
				int hai = list.get(i) / 4;
				g.drawImage(getImage(hai), 0 + HAI_W * i, 500, null);
			}
			break;
		case 2:
			for (int i = 0; i < list.size(); i++) {
				g.drawImage(getImage(imgHaiBackV), 0 + HAI_W * i, 100, null);
			}
			break;
		// TODO kamicha, shimocha
		case 1:
			for (int i = 0; i < list.size(); i++) {
				g.drawImage(getImage(imgHaiBackH), 500, 0 + HAI_W * i, null);
			}
		case 3:
		}
	}

	private int imgHaiBackV, imgHaiBackH;

	@Override
	protected void setupLoadResource(SpriteSet spriteSet, SoundSet soundSet) {
		final char[] color = new char[] { 'm', 'p', 's' };
		final String[] zi = new String[] { "ji_e", "ji_s", "ji_w", "ji_n",
				"no", "ji_h", "ji_c" };
		for (int i = 0; i < 27; i++) {
			String fileName = String.format("res/mj/p_%ss%d_0.gif",
					color[i / 9], i % 9 + 1);
			spriteSet.add(fileName, HAI_W, HAI_H);
		}
		for (int i = 0; i < 7; i++) {
			String fileName = String.format("res/mj/p_%s_0.gif", zi[i]);
			spriteSet.add(fileName, HAI_W, HAI_H);
		}
		imgHaiBackV = spriteSet.add("res/mj/p_bk_0.gif", HAI_W, HAI_H);
		imgHaiBackH = spriteSet.add("res/mj/p_bk_3.gif", 44, 49);
	}

}
