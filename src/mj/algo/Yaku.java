package mj.algo;

/**
 * @author yappy
 */
public enum Yaku {

	PINFU(1), TANYAO(1), IPEKO(1), RICHI(1), IPPATSU(1), TSUMO(1), YAKU(1), HAITEI(
			1), RINSHAN(1), CHANKAN(1),

	DOUBLE_RICHI(2), CHITOI(2), CHANTA(2), ITTSU(2), DOJUN(2), DOKO(2), TOITOI(
			2), SANANKO(2), SANKANTSU(2), SHOSANGEN(2), HONROTO(2),

	RYANPEKO(3), HONITSU(3), JUNCHAN(3),

	CHINITSU(6),

	SUANKO(13), SUKANTSU(13), DAISANGEN(13), KOKUSHI(13), TENHO(13), CHIHO(13), SHOSUSHI(
			13), DAISUSHI(13), TSUISO(13), CHINROTO(13), RYUISO(13), CHUREN(13);

	private int han;

	private Yaku(int han) {
		this.han = han;
	}

	public int getHan() {
		return han;
	}

}
