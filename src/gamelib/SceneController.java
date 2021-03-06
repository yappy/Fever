package gamelib;

/**
 * @author yappy
 */
public interface SceneController {

	/**
	 * Clear stack and push scene.
	 * @param scene
	 */
	void jumpScene(Scene scene) throws GameLibException, GameException;

	/**
	 * Push scene.
	 * @param scene
	 */
	void pushScene(Scene scene) throws GameLibException, GameException;

	void popScene() throws GameLibException, GameException;

	void popScene(int n) throws GameLibException, GameException;

	/**
	 * Clear stack(program will be quit).
	 */
	void quit() throws GameLibException, GameException;

}
