package mj.algo;

/**
 * @author yappy
 */
public class Hora {

	public Mentsu atama;
	public Mentsu[] mentsu = new Mentsu[4];
	private int cursor = 0;

	public void pushMentsu(Mentsu elem) {
		if (cursor >= 4)
			throw new MJAlgorithmException("Mentu full");
		mentsu[cursor++] = elem;
	}

	public void popMentsu() {
		if (cursor <= 0)
			throw new MJAlgorithmException("Mentu full");
		mentsu[--cursor] = null;
	}

	public Hora copy() {
		Hora hora = new Hora();
		hora.atama = this.atama;
		for (int i = 0; i < 4; i++) {
			if (this.mentsu[i] != null) {
				hora.mentsu[i] = this.mentsu[i];
			}
		}
		hora.cursor = this.cursor;
		return hora;
	}

	/**
	 * Count each shuntsu.<br>
	 * 
	 * @return int[3][7]
	 */
	public int[][] createShuntsuTable() {
		int[][] result = new int[3][7];
		for (int i = 0; i < 4; i++) {
			if (mentsu[i].type == Mentsu.Type.SHUNTSU) {
				result[mentsu[i].hai / 9][mentsu[i].hai % 9]++;
			}
		}
		return result;
	}

	/**
	 * Count each kotsu.<br>
	 * 
	 * @return boolean[4][9]
	 */
	public boolean[][] createKotsuTable() {
		boolean[][] result = new boolean[4][9];
		for (int i = 0; i < 4; i++) {
			if (mentsu[i].type == Mentsu.Type.KOTSU
					|| mentsu[i].type == Mentsu.Type.KANTSU) {
				result[mentsu[i].hai / 9][mentsu[i].hai % 9] = true;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "" + atama + mentsu[0] + mentsu[1] + mentsu[2] + mentsu[3];
	}

}
