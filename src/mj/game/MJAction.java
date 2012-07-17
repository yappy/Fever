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
		this(action, id, 0);
	}

	public MJAction(Action action, int id, int haiIndex) {
		this.action = action;
		this.id = id;
		this.haiIndex = haiIndex;
	}

	public static enum Action {
		OK, DISCARD, PON, CHI, ANKAN, KAKAN, DAIMINKAN,
	}

	@Override
	public String toString() {
		return action + "(" + id + ")";
	}

	private static final long serialVersionUID = 3630032894849371616L;

}
