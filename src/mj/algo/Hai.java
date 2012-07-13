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

	public static final int KAZE_T = 27;
	public static final int KAZE_N = 28;
	public static final int KAZE_S = 29;
	public static final int KAZE_P = 30;
	public static final int SANGEN1 = 31;
	public static final int SANGEN2 = 32;
	public static final int SANGEN3 = 33;

	public static String toString(int hai) {
		StringBuilder buf = new StringBuilder(2);
		int color = hai / 9;
		int number = hai % 9 + 1;
		buf.append(number);
		switch (color) {
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
		int color = hai / 9;
		int number = hai % 9 + 1;
		return color != COLOR_Z && number != 1 && number != 9;
	}

	public static boolean isChunchanStart(int hai) {
		int color = hai / 9;
		int number = hai % 9 + 1;
		return color != COLOR_Z && number != 1 && number != 7;
	}

	public static boolean isYaochu(int hai) {
		return !isChunchan(hai);
	}

	public static boolean isYaochuStart(int hai) {
		return !isChunchanStart(hai);
	}

	public static boolean isRoto(int hai) {
		int color = hai / 9;
		int number = hai % 9 + 1;
		return color != COLOR_Z && (number == 1 || number == 9);
	}

	public static boolean isManzu(int hai) {
		return hai / 9 == COLOR_M;
	}

	public static boolean isZihai(int hai) {
		return hai / 9 == COLOR_Z;
	}

	public static boolean isKazehai(int hai) {
		return hai >= 27 && hai <= 30;
	}

	private static final boolean[] GREEN_TABLE = new boolean[] { false, true,
			true, true, false, true, false, true, false };

	public static boolean isGreen(int hai) {
		int color = hai / 9;
		int number = hai % 9 + 1;
		return (color == COLOR_S && GREEN_TABLE[number - 1]) || hai == 32;
	}

	public static boolean isGreenStart(int hai) {
		return hai == COLOR_S * 9 + 1;
	}

	public static boolean isSangen(int hai) {
		return hai >= SANGEN1 && hai <= SANGEN3;
	}

}
