package mj.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import mj.algo.Mentsu.Type;

/**
 * @author yappy
 */
public final class MJAlgorithm {

	public static HoraPoint maxPoint(List<Integer> tehai, int agari,
			boolean tsumo) {
		List<Mentsu> nakiList = Collections.emptyList();
		return maxPoint(tehai, nakiList, agari, tsumo);
	}

	public static HoraPoint maxPoint(List<Integer> tehai,
			List<Mentsu> nakiList, int agari, boolean tsumo) {
		Collections.sort(tehai);
		if (Collections.binarySearch(tehai, agari) < 0)
			throw new MJAlgorithmException("Invalid agari hai");

		HoraPoint maxPoint = new HoraPoint();
		// kokushi
		EnumSet<Yaku> kokushiSet = yakuSetKokushi(tehai, tsumo);
		if (!kokushiSet.isEmpty()) {
			HoraPoint point = new HoraPoint(0, kokushiSet, false);
			maxPoint = (point.compareTo(maxPoint) > 0) ? point : maxPoint;
		}
		// chitoi
		EnumSet<Yaku> chitoiSet = yakuSetChitoi(tehai, tsumo);
		if (!chitoiSet.isEmpty()) {
			HoraPoint point = new HoraPoint(25, chitoiSet, false);
			maxPoint = (point.compareTo(maxPoint) > 0) ? point : maxPoint;
		}
		// others
		// StopWatch watch = new StopWatch().start();
		List<Hora> result = enumHora(tehai, nakiList);
		// watch.stop("eunmHora()");
		for (Hora hora : result) {
			// get yaku list
			// System.out.println(hora);
			EnumSet<Yaku> yakuSet = enumYaku(hora, agari, tsumo);
			// System.out.println(yakuSet);

			// fu calculus
			int fuBase = 20;
			int fuMenzanRon = (!hora.isNaki() && !tsumo) ? 10 : 0;
			int fuTsumo = tsumo ? 2 : 0;
			// atama fu
			// TODO: kazehai fu
			int fuAtama = 0;
			if (Hai.isSangen(hora.atama.hai)) {
				fuAtama = 2;
			} else if (Hai.isKazehai(hora.atama.hai)) {
				fuAtama = 2;
			}
			// mentsu fu
			// this must be corrected if ron
			int fuMentsu = 0;
			for (Mentsu m : hora.mentsu) {
				if (m.type == Type.KOTSU) {
					int plus = 2;
					if (!m.naki)
						plus *= 2;
					if (Hai.isYaochu(m.hai))
						plus *= 2;
					if (m.kan)
						plus *= 4;
					fuMentsu += plus;
				}
			}
			// agari fu
			// try all forms (ryanmen, kanchan, ...)
			// and add pinfu
			// try tanki
			if (hora.atama.hai == agari) {
				int fu = fuBase + fuMenzanRon + fuTsumo + fuAtama + fuMentsu
						+ 2;
				fu = (fu + 9) / 10 * 10;
				HoraPoint point = new HoraPoint(fu, yakuSet, hora.isNaki());
				maxPoint = (point.compareTo(maxPoint) > 0) ? point : maxPoint;
			}
			// try mentsu
			for (Mentsu m : hora.mentsu) {
				// exclude naki mentsu
				if (m.naki)
					continue;
				if (m.type == Type.KOTSU && m.hai == agari) {
					// anko -> minko correction
					int fuMentsuDiff = 0;
					if (!tsumo) {
						fuMentsuDiff = -2;
						if (Hai.isYaochu(m.hai))
							fuMentsuDiff *= 2;
					}
					int fu = fuBase + fuMenzanRon + fuTsumo + fuAtama
							+ fuMentsu + fuMentsuDiff;
					fu = (fu + 9) / 10 * 10;
					HoraPoint point = new HoraPoint(fu, yakuSet, hora.isNaki());
					maxPoint = (point.compareTo(maxPoint) > 0) ? point
							: maxPoint;
				} else if (m.type == Type.SHUNTSU) {
					for (int k = 0; k < 3; k++) {
						if (m.hai + k == agari) {
							int fuMachi;
							if (k == 1) {
								// kanchan
								fuMachi = 2;
							} else {
								// ryanmen or penchan
								if ((m.hai % 9 == 0 && agari % 9 == 2)
										|| (m.hai % 9 == 6 && agari % 9 == 6)) {
									fuMachi = 2;
								} else {
									fuMachi = 0;
								}
							}
							// pinfu
							EnumSet<Yaku> pinfuedSet = yakuSet.clone();
							int pinfuTsumo = 0;
							if (fuAtama == 0 && fuMentsu == 0 && fuMachi == 0) {
								if (!hora.isNaki()) {
									pinfuedSet.add(Yaku.PINFU);
									pinfuTsumo = -2;
								}
							}
							int fu = fuBase + fuMenzanRon + fuTsumo
									+ pinfuTsumo + fuAtama + fuMentsu + fuMachi;
							// System.err.println(fuBase);
							// System.err.println(fuMenzanRon);
							// System.err.println(fuTsumo);
							// System.err.println(pinfuTsumo);
							// System.err.println(fuAtama);
							// System.err.println(fuMentsu);
							// System.err.println(fuMachi);
							// System.err.println(fu);
							fu = (fu + 9) / 10 * 10;
							// naki pinfu
							if (fu == 20 && hora.isNaki()) {
								fu = 30;
							}
							HoraPoint point = new HoraPoint(fu, pinfuedSet,
									hora.isNaki());
							maxPoint = (point.compareTo(maxPoint) > 0) ? point
									: maxPoint;
						}
					}
				}
			}
		}
		return maxPoint;
	}

	private static EnumSet<Yaku> yakuSetKokushi(List<Integer> tehai,
			boolean tsumo) {
		if (tehai.size() != 14) {
			return EnumSet.noneOf(Yaku.class);
		}
		int table[] = new int[34];
		for (int hai : tehai) {
			if (!Hai.isYaochu(hai)) {
				return EnumSet.noneOf(Yaku.class);
			}
			table[hai]++;
		}
		boolean once = true;
		for (int count : table) {
			if (count == 2) {
				if (once) {
					once = false;
				} else {
					return EnumSet.noneOf(Yaku.class);
				}
			} else if (count >= 3) {
				return EnumSet.noneOf(Yaku.class);
			}
		}
		EnumSet<Yaku> result = EnumSet.of(Yaku.KOKUSHI);
		if (tsumo) {
			result.add(Yaku.MENZEN_TSUMO);
		}
		return result;
	}

	/**
	 * @param tehai
	 *            This is must be sorted.
	 * @return Yaku set.
	 */
	private static EnumSet<Yaku> yakuSetChitoi(List<Integer> tehai,
			boolean tsumo) {
		if (tehai.size() != 14) {
			return EnumSet.noneOf(Yaku.class);
		}
		for (int i = 0; i < 14; i += 2) {
			if (tehai.get(i) != tehai.get(i + 1)) {
				return EnumSet.noneOf(Yaku.class);
			}
		}
		// forbid using same 4 hai
		for (int i = 0; i < 12; i += 2) {
			if (tehai.get(i) == tehai.get(i + 2)) {
				return EnumSet.noneOf(Yaku.class);
			}
		}
		// OK
		EnumSet<Yaku> result = EnumSet.of(Yaku.CHITOI);
		if (tsumo) {
			result.add(Yaku.MENZEN_TSUMO);
		}
		boolean tanyao = true;
		boolean honro = true;
		boolean tsuiso = true;
		for (int hai : tehai) {
			if (!Hai.isChunchan(hai)) {
				tanyao = false;
			}
			if (!Hai.isYaochu(hai)) {
				honro = false;
			}
			if (!Hai.isZihai(hai)) {
				tsuiso = false;
			}
		}
		if (tanyao) {
			result.add(Yaku.TANYAO);
		}
		if (honro) {
			result.add(Yaku.HONROTO);
		}
		if (tsuiso) {
			result.add(Yaku.TSUISO);
		}
		normalizeYakuSet(result);

		return result;
	}

	/**
	 * Enumerate yaku. Pinfu is excluded. Yakuhai is all included. CHITOI,
	 * KOKUSHI, CHUREN is excluded.
	 * 
	 * @param hora
	 *            Hora form.
	 * @param agari
	 *            Agari hai.
	 * @param tsumo
	 *            tsumo or ron.
	 * @return Yaku set.
	 */
	private static EnumSet<Yaku> enumYaku(Hora hora, int agari, boolean tsumo) {
		EnumSet<Yaku> set = EnumSet.noneOf(Yaku.class);

		boolean naki = hora.isNaki();
		int[][] shuntsuTable = hora.createShuntsuTable();
		boolean[][] kotsuTable = hora.createKotsuTable();

		// TANYAO
		if (Hai.isChunchan(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				switch (m.type) {
				case KOTSU:
					if (!Hai.isChunchan(m.hai))
						ok = false;
					break;
				case SHUNTSU:
					if (!Hai.isChunchanStart(m.hai))
						ok = false;
					break;
				default:
					assert false;
				}
			}
			if (ok) {
				set.add(Yaku.TANYAO);
			}
		}
		// IPEKO
		if (!naki) {
			boolean ok = false;
			for (int k = 0; k < 3; k++) {
				for (int n = 0; n < 7; n++) {
					if (shuntsuTable[k][n] >= 2) {
						ok = true;
						break;
					}
				}
			}
			if (ok) {
				set.add(Yaku.IPEKO);
			}
		}
		// TSUMO
		if (!naki && tsumo) {
			set.add(Yaku.MENZEN_TSUMO);
		}
		// YAKUHAI
		// TODO: kazehai
		{
			for (Mentsu m : hora.mentsu) {
				if (m.type == Mentsu.Type.KOTSU) {
					if (Hai.isZihai(m.hai)) {
						switch (m.hai % 9) {
						case 0:
							set.add(Yaku.YAKU_0);
							break;
						case 1:
							set.add(Yaku.YAKU_1);
							break;
						case 2:
							set.add(Yaku.YAKU_2);
							break;
						case 3:
							set.add(Yaku.YAKU_3);
							break;
						case 4:
							set.add(Yaku.YAKU_4);
							break;
						case 5:
							set.add(Yaku.YAKU_5);
							break;
						case 6:
							set.add(Yaku.YAKU_6);
							break;
						default:
							assert false;
						}
					}
				}
			}
		}

		// CHANTA
		if (Hai.isYaochu(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				switch (m.type) {
				case KOTSU:
					if (!Hai.isYaochu(m.hai))
						ok = false;
					break;
				case SHUNTSU:
					if (!Hai.isYaochuStart(m.hai))
						ok = false;
					break;
				default:
					assert false;
				}
			}
			if (ok) {
				set.add(Yaku.CHANTA);
			}
		}
		// ITTSU
		for (int k = 0; k < 3; k++) {
			if (shuntsuTable[k][0] >= 1 && shuntsuTable[k][3] >= 1
					&& shuntsuTable[k][6] >= 1) {
				set.add(Yaku.ITTSU);
				break;
			}
		}
		// DOJUN
		for (int n = 0; n < 7; n++) {
			if (shuntsuTable[0][n] >= 1 && shuntsuTable[1][n] >= 1
					&& shuntsuTable[2][n] >= 1) {
				set.add(Yaku.DOJUN);
				break;
			}
		}
		// DOKO
		for (int n = 0; n < 9; n++) {
			if (kotsuTable[0][n] && kotsuTable[1][n] && kotsuTable[2][n]) {
				set.add(Yaku.DOKO);
			}
		}
		// TOITOI
		{
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				if (m.type != Mentsu.Type.KOTSU) {
					ok = false;
					break;
				}
			}
			if (ok) {
				set.add(Yaku.TOITOI);
			}
		}
		// SANANKO
		{
			int count = 0;
			for (Mentsu m : hora.mentsu) {
				if (m.type == Mentsu.Type.KOTSU) {
					if (!m.naki && (m.hai != agari || tsumo)) {
						count++;
					}
				}
			}
			if (count >= 3) {
				set.add(Yaku.SANANKO);
			}
		}
		// SANKANTSU
		{
			int count = 0;
			for (Mentsu m : hora.mentsu) {
				if (m.type == Mentsu.Type.KOTSU && m.kan) {
					count++;
				}
			}
			if (count >= 3) {
				set.add(Yaku.SANKANTSU);
			}
		}
		// SHOSANGEN
		if (Hai.isSangen(hora.atama.hai)) {
			int count = 0;
			for (int i = 0; i < 3; i++) {
				if (kotsuTable[3][4 + i]) {
					count++;
				}
			}
			if (count >= 2) {
				set.add(Yaku.SHOSANGEN);
			}
		}
		// HONROTO
		if (Hai.isYaochu(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				if (m.type != Mentsu.Type.KOTSU) {
					ok = false;
					break;
				}
				if (!Hai.isYaochu(m.hai)) {
					ok = false;
					break;
				}
			}
			if (ok) {
				set.add(Yaku.HONROTO);
			}
		}

		// RYANPEKO
		if (!naki) {
			int count = 0;
			for (int k = 0; k < 3; k++) {
				for (int n = 0; n < 7; n++) {
					count += shuntsuTable[k][n] / 2;
				}
			}
			if (count >= 2) {
				set.add(Yaku.RYANPEKO);
			}
		}
		// HONITSU
		{
			boolean ok = true;
			int color = -1;
			if (hora.atama.hai / 9 != 3) {
				color = hora.atama.hai / 9;
			}
			for (Mentsu m : hora.mentsu) {
				if (m.hai / 9 != 3) {
					if (color == -1) {
						color = m.hai / 9;
					} else {
						if (m.hai / 9 != color) {
							ok = false;
							break;
						}
					}
				}
			}
			if (ok) {
				set.add(Yaku.HONITSU);
			}
		}
		// JUNCHAN
		if (Hai.isRoto(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				switch (m.type) {
				case KOTSU:
					if (!Hai.isRoto(m.hai))
						ok = false;
					break;
				case SHUNTSU:
					if (!Hai.isYaochuStart(m.hai))
						ok = false;
					break;
				default:
					assert false;
				}
			}
			if (ok) {
				set.add(Yaku.JUNCHAN);
			}
		}

		// CHINITSU
		if (hora.atama.hai / 9 != Hai.COLOR_Z) {
			boolean ok = true;
			int color = hora.atama.hai / 9;
			for (Mentsu m : hora.mentsu) {
				if (m.hai / 9 != Hai.COLOR_Z) {
					if (m.hai / 9 != color) {
						ok = false;
						break;
					}
				} else {
					ok = false;
					break;
				}
			}
			if (ok) {
				set.add(Yaku.CHINITSU);
			}
		}

		// SUANKO
		if (!naki) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				if (m.type != Mentsu.Type.KOTSU || (m.hai == agari && !tsumo)) {
					ok = false;
					break;
				}
			}
			if (ok) {
				set.add(Yaku.SUANKO);
			}
		}
		// SUKANTSU
		{
			int count = 0;
			for (Mentsu m : hora.mentsu) {
				if (m.type == Mentsu.Type.KOTSU && m.kan) {
					count++;
				}
			}
			if (count >= 4) {
				set.add(Yaku.SUKANTSU);
			}
		}
		// DAISANGEN
		{
			int count = 0;
			for (int i = 0; i < 3; i++) {
				if (kotsuTable[3][4 + i]) {
					count++;
				}
			}
			if (count >= 3) {
				set.add(Yaku.DAISANGEN);
			}
		}
		// SHOSUSHI
		if (Hai.isKazehai(hora.atama.hai)) {
			int count = 0;
			for (Mentsu m : hora.mentsu) {
				if (Hai.isKazehai(m.hai)) {
					count++;
				}
			}
			if (count >= 3) {
				set.add(Yaku.SHOSUSHI);
			}
		}
		// DAISUSHI
		{
			int count = 0;
			for (Mentsu m : hora.mentsu) {
				if (Hai.isKazehai(m.hai)) {
					count++;
				}
			}
			if (count >= 4) {
				set.add(Yaku.DAISUSHI);
			}
		}
		// TSUISO
		if (Hai.isZihai(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				if (!Hai.isZihai(m.hai)) {
					ok = false;
					break;
				}
			}
			if (ok) {
				set.add(Yaku.TSUISO);
			}
		}
		// CHINROTO
		if (Hai.isRoto(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				if (!Hai.isRoto(m.hai)) {
					ok = false;
					break;
				}
			}
			if (ok) {
				set.add(Yaku.CHINROTO);
			}
		}
		// RYUISO
		if (Hai.isGreen(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				switch (m.type) {
				case KOTSU:
					if (!Hai.isGreen(m.hai))
						ok = false;
					break;
				case SHUNTSU:
					if (!Hai.isGreenStart(m.hai))
						ok = false;
					break;
				default:
					assert false;
				}
			}
			if (ok) {
				set.add(Yaku.CHINROTO);
			}
		}

		normalizeYakuSet(set);

		return set;
	}

	private static void normalizeYakuSet(EnumSet<Yaku> set) {
		// 2
		if (set.contains(Yaku.DOUBLE_RICHI)) {
			set.remove(Yaku.RICHI);
		}
		if (set.contains(Yaku.HONROTO)) {
			set.remove(Yaku.CHANTA);
		}

		// 3
		if (set.contains(Yaku.RYANPEKO)) {
			set.remove(Yaku.IPEKO);
		}
		if (set.contains(Yaku.JUNCHAN)) {
			set.remove(Yaku.CHANTA);
		}

		// 6
		if (set.contains(Yaku.CHINITSU)) {
			set.remove(Yaku.HONITSU);
		}

		// 13
		if (set.contains(Yaku.SUANKO)) {
			set.remove(Yaku.SANANKO);
			set.remove(Yaku.TOITOI);
		}
		if (set.contains(Yaku.SUKANTSU)) {
			set.remove(Yaku.SANKANTSU);
			set.remove(Yaku.TOITOI);
		}
		if (set.contains(Yaku.DAISANGEN)) {
			set.remove(Yaku.SHOSANGEN);
			set.remove(Yaku.YAKU_4);
			set.remove(Yaku.YAKU_5);
			set.remove(Yaku.YAKU_6);
		}
		if (set.contains(Yaku.SHOSUSHI) || set.contains(Yaku.DAISUSHI)) {
			set.remove(Yaku.HONITSU);
		}
		if (set.contains(Yaku.TSUISO)) {
			set.remove(Yaku.HONITSU);
			set.remove(Yaku.HONROTO);
		}
		if (set.contains(Yaku.CHINROTO)) {
			set.remove(Yaku.JUNCHAN);
			set.remove(Yaku.HONROTO);
			set.remove(Yaku.TOITOI);
		}
		if (set.contains(Yaku.RYUISO)) {
			set.remove(Yaku.HONITSU);
		}
		if (set.contains(Yaku.CHUREN)) {
			set.remove(Yaku.CHINITSU);
		}
	}

	private static List<Hora> enumHora(List<Integer> tehai,
			List<Mentsu> nakiList) {
		if (tehai.size() + nakiList.size() * 3 != 14)
			throw new MJAlgorithmException("Invalid tehai.size");
		int[] table = new int[34];
		for (int h : tehai) {
			table[h]++;
		}
		List<Hora> result = new ArrayList<>();
		Hora work = new Hora();
		for (Mentsu m : nakiList) {
			work.pushMentsu(m);
		}
		tryAtama(result, work, table, tehai.size());
		return result;
	}

	private static void tryAtama(List<Hora> result, Hora work, int[] table,
			int count) {
		assert count % 3 == 2;
		for (int i = 0; i < 34; i++) {
			if (table[i] >= 2) {
				table[i] -= 2;
				work.atama = Mentsu.create(Mentsu.Type.ATAMA, i);
				tryMentsu(result, work, table, count - 2);
				table[i] += 2;
			}
		}
	}

	private static void tryMentsu(List<Hora> result, Hora work, int[] table,
			int count) {
		assert count % 3 == 0;
		if (count == 0) {
			result.add(work.copy());
			return;
		}
		for (int i = 0; i < 34; i++) {
			if (table[i] > 0) {
				// kotu
				if (table[i] >= 3) {
					table[i] -= 3;
					work.pushMentsu(Mentsu.create(Mentsu.Type.KOTSU, i));
					tryMentsu(result, work, table, count - 3);
					work.popMentsu();
					table[i] += 3;
				}
				// shuntsu
				if (i / 9 <= 2 && i % 9 <= 6) {
					if (table[i] >= 1 && table[i + 1] >= 1 && table[i + 2] >= 1) {
						table[i]--;
						table[i + 1]--;
						table[i + 2]--;
						work.pushMentsu(Mentsu.create(Mentsu.Type.SHUNTSU, i));
						tryMentsu(result, work, table, count - 3);
						work.popMentsu();
						table[i]++;
						table[i + 1]++;
						table[i + 2]++;
					}
				}
				break;
			}
		}
	}

}
