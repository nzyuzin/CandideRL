package utility;

public final class KeyDefinitions {	
	
	/* 
	 * DIRECTION_KEYS - array of chars which are handled by method processDirectionKey in GameEngine.
	 * elements of that array are processed into SOUTH, NORTH, WEST, EAST, NORTHWEST, NORTHEAST,
	 *  SOUTHWEST and SOUTHEAST Direction correspondingly.
	 */
	
	final static char[] DIRECTION_KEYS = { 'j', 'k', 'h', 'l', 'y', 'u', 'b', 'n' };
	
	/* 
	 * EXIT_CHARS, EXIT_CODES - arrays of chars and integer number codes correspondingly which are meant to shutdown the game. 
	 */
	
	private final static char[] EXIT_CHARS = { 'q' };
	private final static int[] EXIT_CODES = { };

	private KeyDefinitions() {	}
	
	public static boolean isExitCode(int code) {
		for (int s : EXIT_CODES)
			if (code == s) return true;
		return false;
	}
	
	public static boolean isExitChar(char c) {
		for (char s : EXIT_CHARS)
			if (c == s) return true;
		return false;
	}
	
	public static boolean isDirectionKey(char key) {
		for (char s : DIRECTION_KEYS)
			if (key == s) return true;
		return false;
	}
	
}
