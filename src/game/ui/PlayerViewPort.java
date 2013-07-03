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

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import game.utility.ColoredChar;
import game.utility.interfaces.ViewPort;

public class PlayerViewPort implements ViewPort {
	
	private static MapWindow mapWindow = null;
	
	private static MessagesWindow messagesWindow = null;
	
	private static StatsWindow statsWindow = null;
	
	public PlayerViewPort(CharColor fontColor) {
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
		
	}
	
	public void drawMap(ColoredChar[][] charMap) {
		String[][] stringMap = new String[charMap.length][charMap[0].length];
		CharColor[][] colors = new CharColor[charMap.length][charMap[0].length];
		
		for (int i = 0; i < charMap.length; i++)
			for (int j = 0; j < charMap[0].length; j++) {
				stringMap[i][j] = charMap[i][j].toString();
				colors[i][j] = charMap[i][j].getColor();
			}
		
		mapWindow.drawMap(stringMap, colors);
	}
	
	public void showMessage(String msg) {
		messagesWindow.showMessage(msg);
	}
	
	public void showStats(String stats) {
		statsWindow.showStats(stats);
	}

	@Override
	public void drawBorders() {
		mapWindow.drawBorders();
		messagesWindow.drawBorders();
		statsWindow.drawBorders();
	}

	@Override
	public void redrawContent() {
		drawBorders();
		mapWindow.redraw();
		messagesWindow.redraw();
		statsWindow.redraw();
	}

	@Override
	public void handleInput(char input) {
		// TODO Auto-generated method stub

	}

	public int getMapWidth() {
		return mapWindow.getMapWidth();
	}
	
	public int getMapHeight() {
		return mapWindow.getMapHeight();
	}
	
}
