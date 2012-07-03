package mj.algo;

/**
 * @author yappy
 */
public class StopWatch {

	private long start;

	public StopWatch() {
	}

	public StopWatch start() {
		start = System.nanoTime();
		return this;
	}

	public void stop(String msg) {
		long end = System.nanoTime();
		System.out.printf("%s: %dus%n", msg, (end - start) / 1000);
	}

}
