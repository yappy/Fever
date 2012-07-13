package mj.game;

import ec.util.MersenneTwister;
import gamelib.GameException;
import gamelib.GameLibException;
import gamelib.PrimaryScene;
import gamelib.SceneController;
import gamelib.graphics.SpriteSet;
import gamelib.sound.SoundSet;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import mj.algo.Hai;

/**
 * @author yappy
 */
public class GameScene extends PrimaryScene {

	private MersenneTwister rand;
	private Queue<Integer> yama = new ArrayDeque<>();
	private List<List<Integer>> tehai = new ArrayList<>();

	public GameScene(long randSeed) {
		super("Game Scene", new LoadingRendererImpl());
		initGame(randSeed);
	}

	private void initGame(long randSeed) {
		rand = new MersenneTwister(randSeed);

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

		for (int i = 0; i < 3; i++) {
			List<Integer> hand = new ArrayList<>();
			for (int t = 0; t < 14; t++) {
				hand.add(yama.poll());
			}
			tehai.add(hand);
		}
	}

	@Override
	public void doFrame(SceneController sceneController)
			throws GameLibException, GameException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics2D g) throws GameLibException, GameException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupLoadResource(SpriteSet spriteSet, SoundSet soundSet) {
		// TODO Auto-generated method stub

	}

}
