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
import gamelib.util.Trace;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
	private int playerCount;
	private Queue<Integer> yama = new ArrayDeque<>();
	private List<List<Integer>> tehai = new ArrayList<>();
	private List<List<Integer>> sutehai = new ArrayList<>();
	// ID sequence
	private List<Integer> turnMap = new ArrayList<>();
	// ID (0..2|3)
	private int myID = 0;
	// index of myID in turnMap
	private int myIndex = -1;
	// whose turn (turnMap index)
	private int turnIndex = 0;
	// state
	private MJState mjState = MJState.START;

	/* Action Stream */
	private static final int QUEUE_SIZE = 16;
	private BlockingQueue<MJAction> actionQueue = new ArrayBlockingQueue<>(
			QUEUE_SIZE, true);
	private MJAction[] actionBuffer;

	/* Device */
	// temp
	private static InputDevice input = new InputConfiguration(1, new String[] {
			"", "", "", "", "OK", "Cancel" }).createInputDevice(0);

	private static final int HAI_W = 33;
	private static final int HAI_H = 59;

	public GameScene(long randSeed) {
		super("Game Scene", new LoadingRendererImpl());
		initGame(randSeed);
	}

	private void initGame(long randSeed) {
		rand = new MersenneTwister(randSeed);

		// sanma
		playerCount = 3;
		actionBuffer = new MJAction[playerCount];

		// seat shuffle
		for (int i = 0; i < playerCount; i++) {
			turnMap.add(i);
		}
		Collections.shuffle(turnMap, rand);
		Trace.debug("turnMap: %s", turnMap);
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
		for (int i = 0; i < playerCount; i++) {
			List<Integer> hand = new ArrayList<>();
			for (int t = 0; t < 13; t++) {
				hand.add(yama.poll());
			}
			Collections.sort(hand);
			tehai.add(hand);
		}
		// sutehai
		for (int i = 0; i < playerCount; i++) {
			sutehai.add(new ArrayList<Integer>());
		}
	}

	private void clearActionBuffer() {
		Arrays.fill(actionBuffer, null);
	}

	private void drawHai(int id) {
		assert yama.size() > 0;
		tehai.get(id).add(yama.poll());
	}

	private boolean isMyTurn() {
		return getTurnPlayerID() == myID;
	}

	private int getTurnPlayerID() {
		return turnMap.get(turnIndex);
	}

	private void sendAction(MJAction action) throws GameException {
		// TODO: send to everyone across network
		if (!actionQueue.offer(action))
			throw new GameException();
	}

	@Override
	public void doFrame(SceneController sceneController)
			throws GameLibException, GameException {
		input.poll();

		// poll 1 element
		MJAction nextAction = actionQueue.poll();
		if (nextAction != null) {
			int id = nextAction.id;
			if (id < 0 || id >= playerCount)
				throw new GameException("Invalid ID: " + id);
			if (actionBuffer[id] != null)
				throw new GameException("Buffer is not empty: " + id);
			actionBuffer[id] = nextAction;
		}
		boolean actionAll = true;
		for (MJAction a : actionBuffer) {
			actionAll &= (a != null);
		}

		// state transition
		MJState prev = mjState;
		switch (mjState) {
		case START:
			mjState = MJState.PLAY;
			drawHai(getTurnPlayerID());
			if (!isMyTurn()) {
				sendAction(new MJAction(MJAction.Action.OK, myID));
			}
			// TODO: temp dummy players
			for (int i = 1; i < playerCount; i++) {
				sendAction(new MJAction(MJAction.Action.OK, i));
			}
			break;
		case PLAY:
			if (actionAll) {
				mjState = MJState.REACTION;
				MJAction action = actionBuffer[getTurnPlayerID()];
				int id = action.id;
				int index = action.haiIndex;
				assert id == getTurnPlayerID();
				int tehaiSize = tehai.get(id).size();
				if (index >= tehaiSize)
					throw new GameException("Invalid tehai index");
				sutehai.get(id).add(tehai.get(id).remove(index));
			}
			break;
		case REACTION:
			if (actionAll) {
				mjState = MJState.PLAY;
				turnIndex = (turnIndex + 1) % playerCount;
				drawHai(getTurnPlayerID());
				if (!isMyTurn()) {
					sendAction(new MJAction(MJAction.Action.OK, myID));
				}
				// TODO: temp dummy players
				for (int i = 1; i < playerCount; i++) {
					sendAction(new MJAction(MJAction.Action.OK, i));
				}
			}
			break;
		default:
			assert false;
		}
		if (actionAll) {
			clearActionBuffer();
		}
		if (prev != mjState) {
			Trace.debug("%s -> %s", prev, mjState);
		}

		// intaractive process
		switch (mjState) {
		case PLAY:
			if (isMyTurn()) {
				if (input.isDownFirstAny()) {
					sendAction(new MJAction(MJAction.Action.DISCARD, myID, 1));
				}
			}
			break;
		case REACTION:
			if (!isMyTurn()) {
				if (input.isDownFirstAny()) {
					sendAction(new MJAction(MJAction.Action.OK, myID));
				}
			}
		default:
			break;
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
