package game.ui;

import jcurses.system.*;
import jcurses.util.Rectangle;

// This class is supposed to be hidden from every class other than GameEngine. You shouldn't use it's fields and methods outside of GameEngine.

public class GameUI {
	private static CharColor mapFontColor;
	
	private static int windowWidth;
	private static int windowHeight;
	
	private static int mapWidth;
	private static int mapHeight;
	private static Rectangle mapRectangle = null;
	
	private static int messageWindowWidth;
	private static int messageWindowHeight;
	private static int messageWindowPosition;
	private static Rectangle messageWindowRectangle = null;
	
	private static int statsWindowWidth;
	private static int statsWindowHeight;
	private static Rectangle statsRectangle = null;
	
	private GameUI() {	}
	
	public static void init() {
		Toolkit.init();
		mapFontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		windowWidth = Toolkit.getScreenWidth();
		windowHeight = Toolkit.getScreenHeight();
		
		mapWidth = windowWidth / 5 * 4 - 2;
		mapHeight = windowHeight / 4 * 3 - 2;
		mapRectangle = new Rectangle(1, 1, mapWidth, mapHeight);
		
		messageWindowWidth = windowWidth - 2;
		messageWindowHeight = windowHeight - mapHeight - 4;
		messageWindowPosition = mapHeight + 3;
		messageWindowRectangle = new Rectangle(1, messageWindowPosition, messageWindowWidth, messageWindowHeight);
		
		statsWindowWidth = windowWidth - mapWidth - 4;
		statsWindowHeight = mapHeight;
		statsRectangle = new Rectangle(mapWidth + 3, 1, statsWindowWidth, statsWindowHeight);
		
		drawBorders();
	}
	
	private static void drawBorders() {
		Toolkit.drawBorder(new Rectangle(0, 0, mapWidth + 2, mapHeight + 2), mapFontColor);
		Toolkit.drawBorder(new Rectangle(0, messageWindowPosition - 1, messageWindowWidth + 2, messageWindowHeight + 2), mapFontColor);
		Toolkit.drawBorder(new Rectangle(mapWidth + 2, 0, statsWindowWidth + 2, statsWindowHeight + 2), mapFontColor);
	}
	
	public static void drawMap(String map) {
		Toolkit.printString(map.toString(), mapRectangle, mapFontColor);
	}
	
	public static char getInputChar() {
		InputChar input = Toolkit.readCharacter();
		while (input.isSpecialCode())
			input = Toolkit.readCharacter();
		return input.getCharacter();
	}
	
	public static void showAnnouncement(String msg) {
		Toolkit.clearScreen(mapFontColor);
		Toolkit.printString(msg + " Press spacebar to continue...", 0, 0, mapFontColor);
		waitForChar(' ');
	}
	
	public static void showMessage(String msg) {
		Toolkit.printString(msg, messageWindowRectangle, mapFontColor);
	}
	
	public static void showStats(String stats) {
		Toolkit.printString(stats, statsRectangle, mapFontColor);
	}
	
	private static void waitForChar(char c) {
		while ( getInputChar() != c );
	}
	
	public static void exit() {
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