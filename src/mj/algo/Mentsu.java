package mj.algo;

/**
 * @author yappy
 */
public class Mentsu {

	public static enum Type {
		SHUNTSU, KOTSU, KANTSU, ATAMA,
	}

	public Type type;
	public int number;
	public int kind;
	public boolean naki;

	public Mentsu(Type type, int number, int kind, boolean naki) {
		this.type = type;
		this.number = number;
		this.kind = kind;
		this.naki = naki;
	}

	public Mentsu copy() {
		return new Mentsu(type, number, kind, naki);
	}

}
