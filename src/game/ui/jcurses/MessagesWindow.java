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

package game.ui.jcurses;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;
import java.util.Queue;
import java.util.ArrayDeque;

@Deprecated
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
		StringBuilder messages = new StringBuilder();
		for (String msg : lastMessages)
			messages.append(fitString(msg));
		Toolkit.printString(messages.toString(), messagesRectangle, fontColor);
	}

	void redraw() {
		showMessages();
	}

	private String fitString(String str) {
		assert str.length() <= messagesRectangle.getWidth();
		StringBuilder result = new StringBuilder();
		result.append(str);
		for (int i = str.length(); i < messagesRectangle.getWidth(); i++)
			result.append(" ");

		return result.toString();
	}
}
