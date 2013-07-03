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
import jcurses.util.Rectangle;

public final class StatsWindow extends Rectangle {
	
	private CharColor fontColor = null;
	private Rectangle statsRectangle = null;
	private String stats = null;
	
	StatsWindow(int posX, int posY, int width, int height, CharColor fontColor) {
		super(posX, posY, width, height);
		statsRectangle = new Rectangle( posX + 1, posY + 1, width - 2, height - 2);
		this.fontColor = fontColor;
	}
	
	void drawBorders() {
		Toolkit.drawBorder(this, fontColor);
	}
	
	private String fitString(String str) {
		StringBuffer result = new StringBuffer();
		if ( str.contains("\n")) {
			int nCount = 0;
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == '\n') nCount++;
			String[] splittedString = new String[nCount];
			splittedString = str.split("\n");
			for (int i = 0; i < nCount; i++)
				result.append(fitString(splittedString[i]));
			return result.toString();		
		}
		result.append(str);
		for (int rectWidth = statsRectangle.getWidth(),
				i = str.length() % rectWidth; i < rectWidth; i++)
			result.append(" ");
		return result.toString();
	}
	
	void showStats(String stats) {
		this.stats = fitString(stats);
		Toolkit.printString(this.stats, statsRectangle, fontColor);
	}
	
	void redraw() {
		if (stats != null)
			Toolkit.printString(this.stats, statsRectangle, fontColor);
	}
}
