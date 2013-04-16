package game.ui;

import jcurses.system.*;

// This class is supposed to be hidden from every class other than GameEngine. You shouldn't use it's fields and methods outside of GameEngine.

public class GameUI {
	private static CharColor mapFontColor;
	private static int windowWidth;
	private static int windowHeight;
	private static int mapWidth;
	private static int mapHeight;
	
	private GameUI() { }
	
	public static void init() {
		Toolkit.init();
		mapFontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		windowWidth = Toolkit.getScreenWidth();
		windowHeight = Toolkit.getScreenHeight();
		mapWidth = windowWidth;
		mapHeight = windowHeight;
	}
	
	public static void drawMap(String[] mapInStrings) {
		for (int i = 0; i < mapHeight; i++)
			Toolkit.printString(mapInStrings[i], 0, mapHeight - i - 1, mapFontColor);
	}
	
	public static char getInputChar() {
		InputChar input = Toolkit.readCharacter();
		while (input.isSpecialCode())
			input = Toolkit.readCharacter();
		return input.getCharacter();
	}
	
	public static void showMessage(String msg) {
		Toolkit.clearScreen(mapFontColor);
		Toolkit.printString(msg + " Press spacebar to continue...", 0, 0, mapFontColor);
		waitForChar(' ');
	}
	
	private static void waitForChar(char c) {
		while (true) {
			if (getInputChar() == c)
				break;
		}
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