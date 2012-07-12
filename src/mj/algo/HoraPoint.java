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

	public HoraPoint() {
		this(0, EnumSet.noneOf(Yaku.class), false);
	}

	public HoraPoint(int fu, EnumSet<Yaku> yakuSet, boolean naki) {
		this.han = Yaku.countHan(yakuSet, naki);
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
		int diff = this.basePoint - o.basePoint;
		if (diff != 0)
			return diff;
		diff = this.han - o.han;
		if (diff != 0)
			return diff;
		return this.fu - o.fu;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HoraPoint))
			return false;
		HoraPoint o = (HoraPoint) obj;
		return this.han == o.han && this.fu == o.fu
				&& this.yakuSet.equals(o.yakuSet);
	}

	@Override
	public String toString() {
		return String.format("(%dfu, %dhan, %d, %s)", fu, han, basePoint,
				yakuSet);
	}

	public String toTsumoString() {
		int tsumoKo1 = basePoint;
		tsumoKo1 = (tsumoKo1 + 99) / 100 * 100;
		int tsumoKo2 = basePoint * 2;
		tsumoKo2 = (tsumoKo2 + 99) / 100 * 100;
		int tsumoOya = basePoint * 2;
		tsumoOya = (tsumoOya + 99) / 100 * 100;
		return String.format("Tsumo (%d %d), %dall", tsumoKo1, tsumoKo2,
				tsumoOya);
	}

	public String toRonString() {
		int ronKo = basePoint * 4;
		ronKo = (ronKo + 99) / 100 * 100;
		int ronOya = basePoint * 6;
		ronOya = (ronOya + 99) / 100 * 100;
		return String.format("Ron %d, %d", ronKo, ronOya);
	}
}
