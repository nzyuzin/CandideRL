package game.ui;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

public final class MessagesWindow extends Rectangle {
	private CharColor fontColor = null;
	private Rectangle messagesRectangle = null;
	
	MessagesWindow(int posX, int posY, int width, int height, CharColor fontColor) {
		super(posX, posY, width, height);
		messagesRectangle = new Rectangle( posX + 1, posY + 1, width - 2, height - 2);
		this.fontColor = fontColor;
	}
	
	void drawBorders() {
		Toolkit.drawBorder(this, fontColor);
	}

	void showMessage(String msg) {
		Toolkit.printString(fitString(msg), messagesRectangle, fontColor);
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
		for (int rectWidth = messagesRectangle.getWidth(), i = str.length() % rectWidth; i < rectWidth; i++)
			result.append(" ");
		return result.toString();
	}
}
