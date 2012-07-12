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
	public boolean isKakan;

	private Mentsu(Type type, int hai, boolean naki, int nakiAttr, boolean kan,
			boolean isKakan) {
		this.type = type;
		this.hai = hai;
		this.naki = naki;
		this.nakiAttr = nakiAttr;
		this.kan = kan;
		this.isKakan = isKakan;
	}

	public static Mentsu create(Type type, int hai) {
		return new Mentsu(type, hai, false, 0, false, false);
	}

	public static Mentsu chi(int start, int index) {
		return new Mentsu(Type.SHUNTSU, start, true, index, false, false);
	}

	public static Mentsu pon(int hai, int from) {
		return new Mentsu(Type.KOTSU, hai, true, from, false, false);
	}

	public Mentsu copy() {
		return new Mentsu(type, hai, naki, nakiAttr, kan, isKakan);
	}

	@Override
	public String toString() {
		return "(" + type + ":" + Hai.toString(hai) + ")";
	}

}
