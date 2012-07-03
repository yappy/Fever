package mj.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * @author yappy
 */
public final class MJAlgorithm {

	public static void main(String[] args) {
		List<Integer> tehai;
		tehai = Arrays.asList(1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 10, 10, 4, 4);
		// tehai = Arrays.asList(0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3);
		// List<Hora> result = enumHora(tehai);
		// for (Hora hora : result) {
		// System.out.println(hora);
		// }
		maxPoint(tehai, 4, true);
	}

	public static void maxPoint(List<Integer> tehai, int agari, boolean tsumo) {
		if (tehai.indexOf(agari) == -1)
			throw new MJAlgorithmException("Invalid agari hai");

		List<Hora> result = enumHora(tehai);
		if (result.isEmpty()) {
			System.out.println("Not hora");
			return;
		}

		int hanmax = 0;
		int humax = 0;
		for (Hora hora : result) {
			int han = 0;
			int hu = 0;
			// enumYaku();
			System.out.println(hora);
			System.out.println(enumYaku(hora, tsumo));
			// try all forms (ryanmen, kanchan, ...)
			// and add pinfu
		}
	}

	// except for PINFU
	private static EnumSet<Yaku> enumYaku(Hora hora, boolean tsumo) {
		EnumSet<Yaku> set = EnumSet.noneOf(Yaku.class);

		boolean naki = false;
		for (Mentsu m : hora.mentsu) {
			naki |= m.naki;
		}
		int[][] shuntsuTable = hora.createShuntsuTable();

		// TANYAO
		if (Hai.isChunchan(hora.atama.hai)) {
			boolean ok = true;
			for (Mentsu m : hora.mentsu) {
				switch (m.type) {
				case KOTSU:
				case KANTSU:
					if (!Hai.isChunchan(m.hai)) {
						ok = false;
					}
					break;
				case SHUNTSU:
					if (!Hai.isChunchanStart(m.hai)) {
						ok = false;
					}
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
		// TODO: YAKUHAI
		//

		return set;
	}

	public static List<Hora> enumHora(List<Integer> tehai) {
		if (tehai.size() % 3 != 2 || tehai.size() > 14)
			throw new MJAlgorithmException("Invalid tehai.size");
		int[] table = new int[34];
		for (int h : tehai) {
			table[h]++;
		}
		List<Hora> result = new ArrayList<>();
		tryAtama(result, table, tehai.size());
		return result;
	}

	private static void tryAtama(List<Hora> result, int[] table, int count) {
		assert count % 3 == 2;
		for (int i = 0; i < 34; i++) {
			if (table[i] >= 2) {
				table[i] -= 2;
				Hora work = new Hora();
				work.atama = new Mentsu(Mentsu.Type.ATAMA, i, false);
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
					work.pushMentsu(new Mentsu(Mentsu.Type.KOTSU, i, false));
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
						work.pushMentsu(new Mentsu(Mentsu.Type.SHUNTSU, i,
								false));
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
