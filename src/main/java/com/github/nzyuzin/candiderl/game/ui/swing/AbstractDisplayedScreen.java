/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game.ui.swing;

import com.github.nzyuzin.candiderl.game.ui.DisplayedScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;

public abstract class AbstractDisplayedScreen implements DisplayedScreen {

    protected static final Logger log = LoggerFactory.getLogger(SwingGameUi.class);

    private TextWindow gameWindow;

    public AbstractDisplayedScreen(TextWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    protected TextWindow getGameWindow() {
        return gameWindow;
    }

    protected void writeBlanks(final int times) {
        writeBlackWhite(" ", times);
    }

    protected void writeBlackWhite(final String s) {
        writeBlackWhite(s, 1);
    }

    protected void writeBlackWhite(final String s, final int times) {
        for (int k = 0; k < times; k++) {
            for (int i = 0; i < s.length(); i++) {
                writeBlackWhite(s.charAt(i));
            }
        }
    }

    protected void writeBlackWhite(final char character) {
        gameWindow.write(character, Color.WHITE, Color.BLACK);
    }

    protected void write(final char c, final Color fg, final Color bg) {
        gameWindow.write(c, fg, bg);
    }

    protected void writeBalckWhiteString(final String s) {
        for (int i = 0; i < s.length(); i++) {
            writeBlackWhite(s.charAt(i));
        }
    }

    protected void writeBottomRow(final String s) {
        gameWindow.moveCursorToLastRow();
        writeBalckWhiteString(s);
    }
}
