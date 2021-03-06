package gamelib.util;

/**
 * Version information.
 * @author yappy
 */
public final class Version {

	private Version() {}

	private static final short MAJOR = 0;
	private static final short MINOR = 1;
	private static final int VERSION = MAJOR << 16 | MINOR;

	private static final String VERSION_STRING;
	private static final String LONG_VERSION_STRING;

	static{
		VERSION_STRING = String.format("%d.%d", MAJOR, MINOR);
		LONG_VERSION_STRING = String.format("yappy's GameLibrary%nVersion: %d.%d%n", MAJOR, MINOR);
	}

	public static String getVersionString() {
		return VERSION_STRING;
	}

	public static String getLongVersionString() {
		return LONG_VERSION_STRING;
	}

	public static short getMajorVersion() {
		return MAJOR;
	}

	public static short getMinorVersion() {
		return MINOR;
	}

	public static int getVersion() {
		return VERSION;
	}

}
