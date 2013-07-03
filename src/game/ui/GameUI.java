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

package game.ui;

import jcurses.system.*;

import game.utility.ColoredChar;
import game.utility.interfaces.ViewPort;

// This class is supposed to be hidden from every class other than GameEngine. You shouldn't use its fields and methods outside of GameEngine.

public class GameUI {
	private static CharColor fontColor;
	
	private static PlayerViewPort playerView = null;
	
	private static ViewPort currentView = null;
	
	private GameUI() {	}
	
	public static void init() {
		Toolkit.init();
		fontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		playerView = new PlayerViewPort(fontColor);
	}
	
	private static void drawBorders() {
		playerView.drawBorders();
	}
	
	public static void drawMap(ColoredChar[][] charMap) {
		String[][] stringMap = new String[charMap.length][charMap[0].length];
		CharColor[][] colors = new CharColor[charMap.length][charMap[0].length];
		
		for (int i = 0; i < charMap.length; i++)
			for (int j = 0; j < charMap[0].length; j++) {
				stringMap[i][j] = charMap[i][j].toString();
				colors[i][j] = charMap[i][j].getColor();
			}
		
		currentView = playerView;
		currentView.drawBorders();
		playerView.drawMap(charMap);
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
		playerView.showMessage(msg);
	}
	
	public static void showStats(String stats) {
		playerView.showStats(stats);
	}
	
	private static void waitForChar(char c) {
		while ( getInputChar() != c );
	}
	
	private static void redrawUI() {
		drawBorders();
		currentView.redrawContent();
	}
	
	public static void exit() {
		Toolkit.clearScreen(fontColor);
		Toolkit.shutdown();
	}
	
	public static int getMapWidth() {
		return playerView.getMapWidth();
	}
	
	public static int getMapHeight() {
		return playerView.getMapHeight();
	}

}