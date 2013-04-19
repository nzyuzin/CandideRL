package game.ui;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

public final class StatsWindow extends Rectangle {
	
	private CharColor fontColor = null;
	private Rectangle statsRectangle = null;
	
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
		for (int rectWidth = statsRectangle.getWidth(), i = str.length() % rectWidth; i < rectWidth; i++)
			result.append(" ");
		return result.toString();
	}
	
	void showStats(String stats) {
		Toolkit.printString(fitString(stats), statsRectangle, fontColor);
	}
}
