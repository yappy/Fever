package gamelib;

import java.util.concurrent.Callable;

/**
 * Task to do during "Now Loading".<br>
 * Loading is taken place in sub thread.<br>
 * {@link Callable#call()} to load.<br>
 * {@link #unload()} to unload.<br>
 * {@link Callable#call()} should react to {@link InterruptedException} immediately.
 * @author yappy
 */
public interface LoadingTask extends Callable<Object> {

	/**
	 * Do unload.
	 */
	void unload();

}
