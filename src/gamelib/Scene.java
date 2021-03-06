package gamelib;

import java.awt.Graphics2D;

/**
 * Game scene.<br>
 * A scene has multiple sub-scenes.<br>
 * If scene has one or more sub-scenes, {@link #doFrame(FrameResult)} and {@link #render(Graphics2D)} are not called in that frame.<br>
 * Instead, the stack top of sub-scene's are called.
 * @author yappy
 */
public interface Scene {

	/**
	 * Called at first only once.
	 * @throws GameLibException Exception of this library occured
	 * @throws GameException Other exception occured
	 */
	void init(SceneController sceneController) throws GameLibException, GameException;

	/**
	 * Called at last only once.
	 * @throws GameLibException Exception of this library occured
	 * @throws GameException Other exception occured
	 */
	void destroy() throws GameLibException, GameException;

	/**
	 * Do frame process.
	 * @param result result of this frame
	 * @throws GameLibException
	 */
	void doFrame(SceneController sceneController) throws GameLibException, GameException;

	/**
	 * Renders to g.
	 * @param g rendering target
	 * @throws GameLibException Exception of this library occured
	 * @throws GameException Other exception occured
	 */
	void render(Graphics2D g) throws GameLibException, GameException;

}
