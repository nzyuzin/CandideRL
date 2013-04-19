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
		int len = msg.length(), width = messagesRectangle.getWidth();
		if (len > width) {
			int i;
			for (i = 0; i + width < len; i += width)
				addToLog(msg.substring(i, i + width));
			addToLog(msg.substring(i));
		}
		else addToLog(msg);
		showMessages();
	}
	
	private void addToLog(String message) {
		if (lastMessages.size() == messagesRectangle.getHeight())
			lastMessages.poll();
		lastMessages.add(message);
	}
	
	private void showMessages() {
		StringBuffer messages = new StringBuffer();
		for (String msg : lastMessages)
			messages.append(fitString(msg));
		Toolkit.printString(messages.toString(), messagesRectangle, fontColor);
	}
	
	void redraw() {
		showMessages();
	}
	
	private String fitString(String str) {
		assert str.length() <= messagesRectangle.getWidth();
		StringBuffer result = new StringBuffer();
		result.append(str);
		for (int i = str.length(); i < messagesRectangle.getWidth(); i++)
			result.append(" ");

		return result.toString();
	}
}
