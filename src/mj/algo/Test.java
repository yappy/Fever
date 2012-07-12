package mj.algo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yappy
 */
public class Test {

	// 0-8: mamzu
	// 9-17: pinzu
	// 18-26: sozu
	// 27-33: zihai
	public static void main(String[] args) {
		List<Integer> tehai;
		int agari;
		boolean tsumo;

		// dollar test
		// 11122333m789p77z 2m-T
		tehai = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2, 15, 16, 17, 33, 33);
		agari = 1;
		tsumo = true;
		run(tehai, agari, tsumo);

		// 11122333m678p77z 2m-T
		tehai = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2, 14, 15, 16, 33, 33);
		agari = 1;
		tsumo = true;
		run(tehai, agari, tsumo);

		// kotenho
		// 2234455m234p234s 3m-R
		tehai = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 10, 11, 12, 19, 20, 21);
		agari = 2;
		tsumo = false;
		run(tehai, agari, tsumo);

		// kanchan > ryanmen
		// 22m345p12334s777z 2s-T
		tehai = Arrays.asList(3, 3, 11, 12, 13, 18, 19, 19, 20, 20, 21, 33, 33,
				33);
		agari = 19;
		tsumo = true;
		run(tehai, agari, tsumo);

		// pinfu test
		// 1123m234678p123s 1m-T
		tehai = Arrays
				.asList(0, 0, 0, 1, 2, 10, 11, 12, 14, 15, 16, 18, 19, 20);
		agari = 0;
		run(tehai, agari, true);
		run(tehai, agari, false);

		// naki
		List<Mentsu> naki = Arrays.asList(Mentsu.pon(33, 0), Mentsu.pon(32, 0));
		tehai = Arrays.asList(0, 1, 2, 10, 11, 12, 13, 13);
		agari = 1;
		run(tehai, naki, agari, true);
		run(tehai, naki, agari, false);
	}

	private static void run(List<Integer> tehai, List<Mentsu> naki, int agari,
			boolean tsumo) {
		HoraPoint maxPoint = MJAlgorithm.maxPoint(tehai, naki, agari, tsumo);
		System.out.println(maxPoint);
		if (tsumo) {
			System.out.println(maxPoint.toTsumoString());
		} else {
			System.out.println(maxPoint.toRonString());
		}
		System.out.println();
	}

	private static void run(List<Integer> tehai, int agari, boolean tsumo) {
		run(tehai, Collections.<Mentsu> emptyList(), agari, tsumo);
	}

}
