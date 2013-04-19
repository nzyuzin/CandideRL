package game.ui;

import jcurses.system.*;
import jcurses.util.Rectangle;

public final class MapWindow extends Rectangle {

	private CharColor mapFontColor = null;
	private Rectangle mapRectangle = null;
	private String map = null;
	
	MapWindow(int posX, int posY, int width, int height, CharColor mapFontColor) {
		super(posX, posY, width, height);
		mapRectangle = new Rectangle( posX + 1, posY + 1, width - 2, height - 2);
		this.mapFontColor = mapFontColor;
	}

	void drawMap(String map) {
		this.map = map;
		Toolkit.printString(map, mapRectangle, mapFontColor);
	}
	
	void redraw() {
		if (map != null)
			Toolkit.printString(map, mapRectangle, mapFontColor);
	}
	
	void drawBorders() {
		Toolkit.drawBorder(this, mapFontColor);
	}
	
	int getMapWidth() {
		return mapRectangle.getWidth();
	}
	
	int getMapHeight() {
		return mapRectangle.getHeight();
	}
}
