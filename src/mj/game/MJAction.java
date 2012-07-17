package mj.game;

import java.io.Serializable;

/**
 * @author yappy
 */
public class MJAction implements Serializable {

	public Action action;
	public int id;
	public int haiIndex;

	public MJAction(Action action, int id) {
		this.action = action;
		this.id = id;
	}

	public static enum Action {
		OK, DISCARD, PON, CHI, ANKAN, KAKAN, DAIMINKAN,
	}

	private static final long serialVersionUID = 3630032894849371616L;

}
