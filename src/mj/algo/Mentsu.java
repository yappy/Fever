package mj.algo;

/**
 * @author yappy
 */
public class Mentsu {

	public static enum Type {
		SHUNTSU, KOTSU, KANTSU, ATAMA,
	}

	public Type type;
	public int hai;
	public boolean naki;

	public Mentsu(Type type, int hai, boolean naki) {
		this.type = type;
		this.hai = hai;
		this.naki = naki;
	}

	public Mentsu copy() {
		return new Mentsu(type, hai, naki);
	}

	@Override
	public String toString() {
		return "(" + type + ":" + Hai.toString(hai) + ")";
	}

}
