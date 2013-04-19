package game.ui;

import jcurses.system.*;
import jcurses.util.Rectangle;

public final class MapWindow extends Rectangle {

	CharColor mapFontColor = null;
	Rectangle mapRectangle = null;
	
	public MapWindow(int posX, int posY, int width, int height, CharColor mapFontColor) {
		super(posX, posY, width, height);
		mapRectangle = new Rectangle( posX + 1, posY + 1, width - 2, height - 2);
		this.mapFontColor = mapFontColor;
		// TODO Auto-generated constructor stub
	}

	public void drawMap(String map) {
			Toolkit.printString(map, mapRectangle, mapFontColor);
	}
	
	public void drawBorders() {
		Toolkit.drawBorder(this, mapFontColor);
	}
}
