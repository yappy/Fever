package mj.game;

import ec.util.MersenneTwister;
import gamelib.GameException;
import gamelib.GameLibException;
import gamelib.PrimaryScene;
import gamelib.SceneController;
import gamelib.graphics.SpriteSet;
import gamelib.sound.SoundSet;

import java.awt.Color;
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

	private static final int HAI_W = 33;
	private static final int HAI_H = 59;

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
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 800, 600);
		for (int i = 0; i < 14; i++) {
			g.drawImage(getImage(i), HAI_W * i, 100, null);
		}
	}

	@Override
	protected void setupLoadResource(SpriteSet spriteSet, SoundSet soundSet) {
		for (int i = 0; i < 27; i++) {
			char c = 0;
			switch (i / 9) {
			case 0:
				c = 'm';
				break;
			case 1:
				c = 'p';
				break;
			case 2:
				c = 's';
				break;
			default:
				assert false;
			}
			String fileName = String.format("res/mj/p_%ss%d_0.gif", c,
					i % 9 + 1);
			spriteSet.add(fileName, HAI_W, HAI_H);
		}
	}
}
