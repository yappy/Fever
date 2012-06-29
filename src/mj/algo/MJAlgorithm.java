package mj.algo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yappy
 */
public final class MJAlgorithm {

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
				work.atama = new Mentsu(Mentsu.Type.ATAMA, i % 9, i / 9, false);
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
					work.addMentsu(new Mentsu(Mentsu.Type.SHUNTSU, i % 9,
							i / 9, false));
					tryMentsu(result, work, table, count - 3);
					table[i] += 3;
				}
				// shuntsu
				break;
			}
		}
	}

}
