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
	public int nakiAttr;
	public boolean kan;

	private Mentsu(Type type, int hai, boolean naki, int nakiAttr, boolean kan) {
		this.type = type;
		this.hai = hai;
		this.naki = naki;
		this.nakiAttr = nakiAttr;
		this.kan = kan;
	}

	public static Mentsu create(Type type, int hai) {
		return new Mentsu(type, hai, false, 0, false);
	}

	public Mentsu copy() {
		return new Mentsu(type, hai, naki, nakiAttr, kan);
	}

	@Override
	public String toString() {
		return "(" + type + ":" + Hai.toString(hai) + ")";
	}

}
