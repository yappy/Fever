package gamelib.util;

import ec.util.MersenneTwisterFast;

/**
 * Mersenne Twister random algorithm.<br>
 * This class requires "mt.jar".
 * @author yappy
 */
public final class MTRandom {

	private static MersenneTwisterFast rand = new MersenneTwisterFast();

	private MTRandom() {}

	public static void setSeed(long seed) {
		rand.setSeed(seed);
	}

	public static int nextInt() {
		return rand.nextInt();
	}

	/**
	 * 0 to n-1.
	 */
	public static int nextInt(int n) {
		return rand.nextInt(n);
	}

	public static long nextLong() {
		return rand.nextLong();
	}

	/**
	 * 0 to n-1.
	 */
	public static long nextLong(long n) {
		return rand.nextLong(n);
	}

	/**
	 * [0.0, 1.0)
	 */
	public static double nextDouble() {
		return rand.nextDouble();
	}

	public static boolean nextBoolean() {
		return rand.nextBoolean();
	}

}
