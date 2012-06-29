package mj.algo;

/**
 * @author yappy
 */
public class Hora {

	public Mentsu atama;
	public Mentsu[] mentsu = new Mentsu[4];

	public void addMentsu(Mentsu elem) {
		if (mentsu[3] != null)
			throw new MJAlgorithmException("Mentu full");
		for (int i = 0; i < 4; i++) {
			if (mentsu[i] == null) {
				mentsu[i] = elem;
				break;
			}
		}
	}

	public Hora copy() {
		Hora hora = new Hora();
		hora.atama = this.atama;
		for (int i = 0; i < 4; i++) {
			if (this.mentsu[i] != null) {
				hora.mentsu[i] = this.mentsu[i];
			}
		}
		return hora;
	}

}
