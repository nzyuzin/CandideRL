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

package com.github.nzyuzin.candiderl.game.ui.jcurses;

import jcurses.system.*;
import jcurses.util.Rectangle;

@Deprecated
public final class MapWindow extends Rectangle {

	private CharColor mapBorderColor = null;
	private Rectangle mapRectangle = null;
	private String[][] map = null;
	private CharColor[][] colors = null;

	MapWindow(int posX, int posY, int width, int height, CharColor mapBorderColor) {
		super(posX, posY, width, height);
		mapRectangle = new Rectangle( posX + 1, posY + 1, width - 2, height - 2);
		this.mapBorderColor = mapBorderColor;
	}

	void drawMap(String[][] map, CharColor[][] colors) {

		this.map = map;
		this.colors = colors;

		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				Toolkit.printString(map[i][j], i + 1, j + 1, colors[i][j]);
	}

	void redraw() {
		if (map != null && colors != null)
			drawMap(this.map, this.colors);
	}

	void drawBorders() {
		Toolkit.drawBorder(this, mapBorderColor);
	}

	int getMapWidth() {
		return mapRectangle.getWidth();
	}

	int getMapHeight() {
		return mapRectangle.getHeight();
	}
}
