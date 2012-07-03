package mj.algo;

/**
 * Hai utility. 0-8: mamzu, 9-17: pinzu, 18-26: sozu, 27-33: zihai, 34-35:
 * empty.
 * 
 * @author yappy
 */
public final class Hai {

	public static final int COLOR_M = 0;
	public static final int COLOR_P = 1;
	public static final int COLOR_S = 2;
	public static final int COLOR_Z = 3;

	public static String toString(int hai) {
		StringBuilder buf = new StringBuilder(2);
		int kind = hai / 9;
		int number = hai % 9 + 1;
		buf.append(number);
		switch (kind) {
		case COLOR_M:
			buf.append('m');
			break;
		case COLOR_P:
			buf.append('p');
			break;
		case COLOR_S:
			buf.append('s');
			break;
		case COLOR_Z:
			buf.append('z');
			break;
		default:
			buf.append('?');
		}
		return buf.toString();
	}

	public static boolean isChunchan(int hai) {
		int kind = hai / 9;
		int number = hai % 9 + 1;
		return kind != COLOR_Z && number != 1 && number != 9;
	}

	public static boolean isChunchanStart(int hai) {
		int kind = hai / 9;
		int number = hai % 9 + 1;
		return kind != COLOR_Z && number != 1 && number != 7;
	}

	public static boolean isYaochu(int hai) {
		return !isChunchan(hai);
	}

	public static boolean isYaochuStart(int hai) {
		return !isChunchanStart(hai);
	}

	public static boolean isRoto(int hai) {
		int kind = hai / 9;
		int number = hai % 9 + 1;
		return kind != COLOR_Z && (number == 1 || number == 9);
	}

	public static boolean isZihai(int hai) {
		return hai / 9 == COLOR_Z;
	}

}
