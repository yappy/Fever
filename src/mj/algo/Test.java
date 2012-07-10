package mj.algo;

import java.util.Arrays;
import java.util.List;

/**
 * @author yappy
 */
public class Test {

	public static void main(String[] args) {
		// 0-8: mamzu, 9-17: pinzu, 18-26: sozu, 27-33: zihai, 34-35: empty
		List<Integer> tehai;
		int agari;
		boolean tsumo;

		// tehai = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2, 15, 16, 17, 33, 33);
		// agari = 1;
		// tsumo = true;
		// MJAlgorithm.maxPoint(tehai, agari, tsumo);

		tehai = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2, 14, 15, 16, 33, 33);
		agari = 1;
		tsumo = true;
		MJAlgorithm.maxPoint(tehai, agari, tsumo);
	}

}
