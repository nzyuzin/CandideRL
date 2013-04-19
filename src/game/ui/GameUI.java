package game.ui;

import jcurses.system.*;
import jcurses.util.Rectangle;

// This class is supposed to be hidden from every class other than GameEngine. You shouldn't use it's fields and methods outside of GameEngine.

public class GameUI {
	private static CharColor fontColor;
	
	private static MapWindow mapWindow = null;
	
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
		fontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		int windowWidth = Toolkit.getScreenWidth();
		int windowHeight = Toolkit.getScreenHeight();
		
		int mapWidth = windowWidth / 5 * 4 - 2;
		int mapHeight = windowHeight / 4 * 3 - 2;
		mapWindow = new MapWindow(0, 0, mapWidth, mapHeight, fontColor);
		
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
		mapWindow.drawBorders();
		Toolkit.drawBorder(new Rectangle(0, messageWindowPosition - 1, messageWindowWidth + 2, messageWindowHeight + 2), fontColor);
		Toolkit.drawBorder(new Rectangle(Toolkit.getScreenWidth() + statsWindowWidth, 0, statsWindowWidth + 2, statsWindowHeight + 2), fontColor);
	}
	
	public static void drawMap(String map) {
		mapWindow.drawMap(map);
	}
	
	public static char getInputChar() {
		InputChar input = Toolkit.readCharacter();
		while (input.isSpecialCode())
			input = Toolkit.readCharacter();
		return input.getCharacter();
	}
	
	private static String fitStringIntoRectangle(String str, Rectangle rect) {
		StringBuffer result = new StringBuffer();
		if ( str.contains("\n")) {
			int nCount = 0;
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == '\n') nCount++;
			String[] splittedString = new String[nCount];
			splittedString = str.split("\n");
			for (int i = 0; i < nCount; i++)
				result.append(fitStringIntoRectangle(splittedString[i], rect));
			return result.toString();		
		}
		result.append(str);
		for (int rectWidth = rect.getWidth(), i = str.length() % rectWidth; i < rectWidth; i++)
			result.append(" ");
		return result.toString();
	}
	
	public static void showAnnouncement(String msg) {
		Toolkit.clearScreen(fontColor);
		Toolkit.printString(msg + " Press spacebar to continue...", 0, 0, fontColor);
		waitForChar(' ');
		redrawUI();
	}
	
	public static void showMessage(String msg) {
		Toolkit.printString(msg, messageWindowRectangle, fontColor);
	}
	
	public static void showStats(String stats) {
		Toolkit.printString(stats, statsRectangle, fontColor);
	}
	
	private static void waitForChar(char c) {
		while ( getInputChar() != c );
	}
	
	private static void redrawUI() {
		drawBorders();
		mapWindow.redrawMap();
	}
	
	public static void exit() {
		Toolkit.clearScreen(fontColor);
		Toolkit.shutdown();
	}
	
	public static int getMapWidth() {
		return mapWindow.getMapWidth();
	}
	
	public static int getMapHeight() {
		return mapWindow.getMapHeight();
	}

}