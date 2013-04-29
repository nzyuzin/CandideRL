package game.ui;

import jcurses.system.*;

import game.utility.ColoredChar;

// This class is supposed to be hidden from every class other than GameEngine. You shouldn't use it's fields and methods outside of GameEngine.

public class GameUI {
	private static CharColor fontColor;
	
	private static MapWindow mapWindow = null;
	
	private static MessagesWindow messagesWindow = null;
	
	private static StatsWindow statsWindow = null;
	
	private GameUI() {	}
	
	public static void init() {
		Toolkit.init();
		fontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		int windowWidth = Toolkit.getScreenWidth();
		int windowHeight = Toolkit.getScreenHeight();
		
		int mapWidth = windowWidth / 5 * 4;
		int mapHeight = windowHeight / 4 * 3;
		mapWindow = new MapWindow(0, 0, mapWidth, mapHeight, fontColor);
		
		int messageWindowWidth = windowWidth;
		int messageWindowHeight = windowHeight - mapHeight;
		int messageWindowPosition = mapHeight;
		messagesWindow = new MessagesWindow(0, messageWindowPosition, messageWindowWidth, messageWindowHeight, fontColor);
		
		int statsWindowWidth = windowWidth - mapWidth;
		int statsWindowHeight = mapHeight;
		statsWindow = new StatsWindow(mapWidth, 0, statsWindowWidth, statsWindowHeight, fontColor);
		
		drawBorders();
	}
	
	private static void drawBorders() {
		mapWindow.drawBorders();
		messagesWindow.drawBorders();
		statsWindow.drawBorders();
	}
	
	public static void drawMap(ColoredChar[][] charMap) {
		String[][] stringMap = new String[charMap.length][charMap[0].length];
		CharColor[][] colors = new CharColor[charMap.length][charMap[0].length];
		
		for (int i = 0; i < charMap.length; i++)
			for (int j = 0; j < charMap[0].length; j++) {
				stringMap[i][j] = charMap[i][j].toString();
				colors[i][j] = charMap[i][j].getColor();
			}
		
		mapWindow.drawMap(stringMap, colors);
	}
	
	public static char getInputChar() {
		InputChar input = Toolkit.readCharacter();
		while (input.isSpecialCode())
			input = Toolkit.readCharacter();
		return input.getCharacter();
	}
	
	public static void showAnnouncement(String msg) {
		Toolkit.clearScreen(fontColor);
		Toolkit.printString(msg + " Press spacebar to continue...", 0, 0, fontColor);
		waitForChar(' ');
		redrawUI();
	}
	
	public static void showMessage(String msg) {
		messagesWindow.showMessage(msg);
	}
	
	public static void showStats(String stats) {
		statsWindow.showStats(stats);
	}
	
	private static void waitForChar(char c) {
		while ( getInputChar() != c );
	}
	
	private static void redrawUI() {
		drawBorders();
		mapWindow.redraw();
		messagesWindow.redraw();
		statsWindow.redraw();
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