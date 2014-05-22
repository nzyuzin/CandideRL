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

import com.github.nzyuzin.candiderl.game.ui.GameUI;
import com.github.nzyuzin.candiderl.game.ui.ViewPort;
import jcurses.system.*;

import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

@Deprecated
public class JCursesGameUI implements GameUI {

    private static final JCursesGameUI INSTANCE = new JCursesGameUI();

	private CharColor fontColor;

	private PlayerViewPort playerView = null;

	private ViewPort currentView = null;

	private JCursesGameUI() {
        Toolkit.init();
        fontColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
        playerView = new PlayerViewPort(fontColor);
    }

    public static JCursesGameUI getInstance() {
        return INSTANCE;
    }

	private void drawBorders() {
		playerView.drawBorders();
	}

	public void drawMap(ColoredChar[][] charMap) {
		currentView = playerView;
		playerView.drawMap(charMap);
        currentView.drawBorders();
	}

	public char getInputChar() {
		InputChar input = Toolkit.readCharacter();
		while (input.isSpecialCode())
			input = Toolkit.readCharacter();
		return input.getCharacter();
	}

	public void showAnnouncement(String msg) {
		Toolkit.clearScreen(fontColor);
		Toolkit.printString(msg + " Press spacebar to continue...", 0, 0, fontColor);
		waitForChar(' ');
		redrawUI();
	}

	public void showMessage(String msg) {
		playerView.showMessage(msg);
	}

	public void showStats(String stats) {
		playerView.showStats(stats);
	}

	private void waitForChar(char c) {
        //noinspection StatementWithEmptyBody
        while (getInputChar() != c);
	}

	private void redrawUI() {
		currentView.drawContent();
        drawBorders();
	}

	public void close() {
		Toolkit.shutdown();
	}

    public int getScreenWidth() {
        return Toolkit.getScreenWidth();
    }

    public int getScreenHeight() {
        return Toolkit.getScreenHeight();
    }

	public int getMapWidth() {
		return playerView.getMapWidth();
	}

	public int getMapHeight() {
		return playerView.getMapHeight();
	}

}