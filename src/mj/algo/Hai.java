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

}
