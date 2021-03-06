package gamelib.util;

/**
 * Sleep method without throws declaration.<br>
 * This class can be used for machine spec dependency test.
 * @author yappy
 */
public final class Delay {

	private Delay() {}

	/**
	 * Do nothing for given time.
	 * Call Thread.sleep(ms) and ignore Exceptions.
	 * @param ms sleep time(mili second)
	 */
	public static void delay(long ms) {
		try{
			Thread.sleep(ms);
		}
		catch(InterruptedException e){}
	}

}
