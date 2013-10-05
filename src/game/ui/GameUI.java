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

// TODO: Screw jcurses, use some other library instead

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
		currentView = playerView;
		playerView.drawMap(charMap);
        currentView.drawBorders();
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
		currentView.redrawContent();
        drawBorders();
	}
	
	public static void exit() {
		Toolkit.shutdown();
	}

    public static int getScreenWidth() {
        return Toolkit.getScreenWidth();
    }

    public static int getScreenHeight() {
        return Toolkit.getScreenHeight();
    }
	
	public static int getMapWidth() {
		return playerView.getMapWidth();
	}
	
	public static int getMapHeight() {
		return playerView.getMapHeight();
	}

}