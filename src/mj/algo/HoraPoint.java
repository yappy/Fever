package mj.algo;

import java.util.EnumSet;

/**
 * @see <a
 *      href="http://ja.wikipedia.org/wiki/%E9%BA%BB%E9%9B%80%E3%81%AE%E5%BE%97%E7%82%B9%E8%A8%88%E7%AE%97">
 *      Wikipedia: Majang point calculus</a>
 * @author yappy
 */
public class HoraPoint implements Comparable<HoraPoint> {

	private int han;
	private int fu;
	private EnumSet<Yaku> yakuSet;

	private int basePoint;

	public HoraPoint(int han, int fu, EnumSet<Yaku> yakuSet) {
		this.han = han;
		this.fu = fu;
		this.yakuSet = yakuSet;

		if (han >= 6) {
			switch (han) {
			// haneman
			case 6:
			case 7:
				basePoint = 3000;
				break;
			// baiman
			case 8:
			case 9:
			case 10:
				basePoint = 4000;
				break;
			// sanbaiman
			case 11:
			case 12:
				basePoint = 6000;
				break;
			// N-bai yakuman
			default:
				int n = han / 10;
				basePoint = 8000 * n;
				break;
			}
		} else {
			// normal
			basePoint = fu * 1 << (2 + han);
			basePoint = Math.min(basePoint, 2000);
		}
	}

	public int getHan() {
		return han;
	}

	public int getFu() {
		return fu;
	}

	public EnumSet<Yaku> getYakuSet() {
		return yakuSet;
	}

	public int getBasePoint() {
		return basePoint;
	}

	@Override
	public int compareTo(HoraPoint o) {
		return this.basePoint - o.basePoint;
	}

}
