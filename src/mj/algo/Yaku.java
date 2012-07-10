package mj.algo;

import java.util.EnumSet;

/**
 * Yaku. Honitsu is (2, 1) han in fever.
 * 
 * @author yappy
 */
public enum Yaku {

	PINFU(1, 0), TANYAO(1, 1), IPEKO(1, 0), RICHI(1, 0), IPPATSU(1, 0), MENZEN_TSUMO(
			1, 0), YAKU_0(1, 1), YAKU_1(1, 1), YAKU_2(1, 1), YAKU_3(1, 1), YAKU_4(
			1, 1), YAKU_5(1, 1), YAKU_6(1, 1), HAITEI(1, 1), RINSHAN(1, 1), CHANKAN(
			1, 1),

	DOUBLE_RICHI(2, 0), CHITOI(2, 0), CHANTA(2, 1), ITTSU(2, 1), DOJUN(2, 1), DOKO(
			2, 2), TOITOI(2, 2), SANANKO(2, 2), SANKANTSU(2, 2), SHOSANGEN(2, 2), HONROTO(
			2, 2),

	RYANPEKO(3, 0), HONITSU(2, 1), JUNCHAN(3, 2),

	CHINITSU(6, 5),

	SUANKO(13, 0), SUKANTSU(13, 13), DAISANGEN(13, 13), KOKUSHI(13, 0), TENHO(
			13, 0), CHIHO(13, 0), SHOSUSHI(13, 13), DAISUSHI(26, 26), TSUISO(
			13, 13), CHINROTO(13, 13), RYUISO(13, 13), CHUREN(13, 0);

	private int han, kuiHan;

	private Yaku(int han, int kuiHan) {
		this.han = han;
		this.kuiHan = kuiHan;
	}

	public int getHan() {
		return han;
	}

	public int getKuiHan() {
		return kuiHan;
	}

	public static int countHan(EnumSet<Yaku> yakuSet, boolean naki) {
		int sum = 0;
		for (Yaku yaku : yakuSet) {
			if (naki) {
				if (yaku.getKuiHan() <= 0)
					throw new IllegalArgumentException(
							"Included invalid yaku in naki");
				sum += yaku.getKuiHan();
			} else {
				sum += yaku.getHan();
			}
		}
		return sum;
	}

}
