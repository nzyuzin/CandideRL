package ui;

import jcurses.system.*;

public class GameUI {
	private static CharColor mapFontColor;
	private static int windowWidth;
	private static int windowHeight;
	private final static char[] EXIT_CHARS = {' '};
	private final static int[] EXIT_CODES = {};
	private static int mapWidth;
	private static int mapHeight;
	
	public static void init() {
		Toolkit.init();
		mapFontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		windowWidth = Toolkit.getScreenWidth();
		windowHeight = Toolkit.getScreenHeight();
		mapWidth = windowWidth;
		mapHeight = windowHeight;
	}
	
	public static void drawMap(String[] mapInStrings) {
		for (int i = 0; i < windowHeight; i++)
			Toolkit.printString(mapInStrings[i], 0, windowHeight - i - 1, mapFontColor);
	}
	
	public static char getInputChar() {
		InputChar input = Toolkit.readCharacter();
		while (input.isSpecialCode())
			input = Toolkit.readCharacter();
		return input.getCharacter();
	}
	
	public static void showMessage(String msg) {
		Toolkit.clearScreen(mapFontColor);
		Toolkit.printString(msg, 0, 0, mapFontColor);
		waitForExitChar();
	}
	
	private static void waitForExitChar() {
		InputChar input = Toolkit.readCharacter();
		while (true) {
			input = Toolkit.readCharacter();
			if (input.isSpecialCode())
				if (isExitCode(input.getCode()))
					break;
			if (isExitChar(input.getCharacter()))
				break;
		}
	}
	
	private static boolean isExitCode(int code) {
		for (int s : EXIT_CODES)
			if (code == s) return true;
		return false;
	}
	
	private static boolean isExitChar(char c) {
		for (char s : EXIT_CHARS)
			if (c == s) return true;
		return false;
	}
	
	public static void close() {
		Toolkit.clearScreen(mapFontColor);
		Toolkit.shutdown();
	}
	
	public static int getMapWidth() {
		return mapWidth;
	}
	
	public static int getMapHeight() {
		return mapHeight;
	}

}