package game.ui;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;
import java.util.Queue;
import java.util.ArrayDeque;

public final class MessagesWindow extends Rectangle {
	private CharColor fontColor = null;
	private Rectangle messagesRectangle = null;
	private Queue<String> lastMessages = null;
	
	MessagesWindow(int posX, int posY, int width, int height, CharColor fontColor) {
		super(posX, posY, width, height);
		lastMessages = new ArrayDeque<String>();
		messagesRectangle = new Rectangle( posX + 1, posY + 1, width - 2, height - 2);
		this.fontColor = fontColor;
	}
	
	void drawBorders() {
		Toolkit.drawBorder(this, fontColor);
	}

	void showMessage(String msg) {
		addToLog(fitString(msg));
		showMessages();
	}
	
	private void addToLog(String message) {
		if (lastMessages.size() == messagesRectangle.getHeight())
			lastMessages.poll();
		lastMessages.add(message);
	}
	
	private void showMessages() {
		StringBuffer log = new StringBuffer();
		for (String msg : lastMessages) {
			log.append(msg);
			log.append("\n");
		}
		
		Toolkit.printString(log.toString(), messagesRectangle, fontColor);
	}
	
	void redraw() {
		showMessages();
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
