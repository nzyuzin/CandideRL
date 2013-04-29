package game.ui;

import jcurses.system.*;
import jcurses.util.Rectangle;

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
