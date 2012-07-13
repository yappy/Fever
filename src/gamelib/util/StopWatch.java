package gamelib.util;

/**
 * @author yappy
 * 
 */
public final class StopWatch {

	public StopWatch() {}

	private long prev;

	public void start() {
		prev = System.nanoTime();
	}

	public long stopNs() {
		return System.nanoTime() - prev;
	}

	public double stopMs() {
		return stopNs() / 1e6;
	}

	public double stopFps() {
		return 1e9 / stopNs();
	}

}
