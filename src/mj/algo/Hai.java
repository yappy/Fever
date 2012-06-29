package mj.algo;

/**
 * 
 * @author yappy
 */
public final class Hai {

	public static String toString(int hai) {
		StringBuilder buf = new StringBuilder(2);
		int kind = hai / 9;
		int number = hai % 9 + 1;
		buf.append(number);
		switch (kind) {
		case 0:
			buf.append('m');
			break;
		case 1:
			buf.append('p');
			break;
		case 2:
			buf.append('s');
			break;
		case 3:
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
		return kind <= 2 && number != 1 && number != 9;
	}
	
	public static boolean isChunchanStart(int hai) {
		int kind = hai / 9;
		int number = hai % 9 + 1;
		return kind <= 2 && number != 1 && number != 7;
	}

	public static boolean isYaochu(int hai) {
		return !isChunchan(hai);
	}

}
