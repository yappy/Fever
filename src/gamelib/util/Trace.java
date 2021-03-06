package gamelib.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Debug output.<br>
 * You can output to console for IDE, also can output to file for distribution version.
 * @author yappy
 */
public final class Trace {

	private static boolean isDebug = true;
	private static PrintStream fileOut = null;

	private Trace() {}

	public static void setDebug(boolean isDebug) {
		Trace.isDebug = isDebug;
	}

	public static void setOutFile(File outFile) throws IOException {
		if(outFile != null)
			fileOut = new PrintStream(new FileOutputStream(outFile), true);
		else
			fileOut = null;
	}

	/**
	 * Output message.(ignored if debug mode)
	 * @param str message
	 */
	public static void debug(String str) {
		if(!isDebug)
			return;
		info(str);
	}

	public static void debug(String format, Object... args) {
		debug(String.format(format, args));
	}

	/**
	 * Output message.(not ignored even debug mode)
	 * @param str message
	 */
	public static void info(String str) {
		System.out.println(str);
		if(fileOut != null)
			fileOut.println(str);
	}

	public static void info(String format, Object... args) {
		info(String.format(format, args));
	}

	/**
	 * Output stack trace of t.
	 * @param t Throwable object
	 */
	public static void error(Throwable t) {
		t.printStackTrace();
		if(fileOut != null)
			t.printStackTrace(fileOut);
	}

}
