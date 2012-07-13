package gamelib;

import gamelib.util.Trace;

/**
 * Defualt partical implementation of {@link Scene}.
 * @author yappy
 */
public abstract class AbstractScene implements Scene {

	/** Scene name for debug output. */
	private String name;
	/** doFrame count. */
	private int frameCount = 0;

	protected AbstractScene(String name) {
		this.name = name;
	}

	@Override
	public void init(SceneController sceneController) throws GameLibException, GameException {
		Trace.debug("Init scene: " + name);
	}

	@Override
	public void destroy() {
		Trace.debug("Destroy scene: " + name);
	}

	protected int countUp() {
		return frameCount++;
	}

	protected int getFrameCount() {
		return frameCount;
	}

}
