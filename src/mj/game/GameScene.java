package mj.game;

import gamelib.GameException;
import gamelib.GameLibException;
import gamelib.PrimaryScene;
import gamelib.SceneController;
import gamelib.graphics.SpriteSet;
import gamelib.sound.SoundSet;

import java.awt.Graphics2D;

/**
 * @author yappy
 */
public class GameScene extends PrimaryScene {

	public GameScene() {
		super("Game Scene", new LoadingRendererImpl());
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
