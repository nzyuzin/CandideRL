/*
 *  This file is part of CandideRL.
 *
 *  CandideRL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CandideRL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.utility;

public final class KeyDefinitions {	
	
	/* 
	 * DIRECTION_KEYS - array of chars which are handled by method processDirectionKey in GameEngine.
	 * elements of that array are processed into SOUTH, NORTH, WEST, EAST, NORTHWEST, NORTHEAST,
	 *  SOUTHWEST and SOUTHEAST Direction respectively.
	 */
	
	final static char[] DIRECTION_KEYS = { 'j', 'k', 'h', 'l', 'y', 'u', 'b', 'n' };
	
	/* 
	 * EXIT_CHARS, EXIT_CODES - arrays of chars and integer number codes respectively which are meant to shutdown the game. 
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
