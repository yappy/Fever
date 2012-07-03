package mj.algo;

/**
 * @author yappy
 */
public class Mentsu {

	public static enum Type {
		SHUNTSU, KOTSU, ATAMA,
	}

	public Type type;
	public int hai;
	public boolean naki;
	public boolean kan;

	public Mentsu(Type type, int hai, boolean naki, boolean kan) {
		this.type = type;
		this.hai = hai;
		this.naki = naki;
		this.kan = kan;
	}

	public Mentsu copy() {
		return new Mentsu(type, hai, naki, kan);
	}

	@Override
	public String toString() {
		return "(" + type + ":" + Hai.toString(hai) + ")";
	}

}
