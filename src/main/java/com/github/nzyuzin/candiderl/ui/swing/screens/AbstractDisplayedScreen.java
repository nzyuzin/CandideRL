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

package com.github.nzyuzin.candiderl.ui.swing.screens;

import com.github.nzyuzin.candiderl.ui.DisplayedScreen;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.Map;

public abstract class AbstractDisplayedScreen implements DisplayedScreen {

    protected static final Logger log = LoggerFactory.getLogger(AbstractDisplayedScreen.class);

    private TextWindow gameWindow;

    public AbstractDisplayedScreen(TextWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    protected TextWindow getGameWindow() {
        return gameWindow;
    }

    protected void clearScreen() {
        gameWindow.clearScreen();
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

    protected void writeBlackWhiteString(final String s) {
        for (int i = 0; i < s.length(); i++) {
            writeBlackWhite(s.charAt(i));
        }
    }

    protected void nextLine() {
        gameWindow.nextRow();
    }

    protected void writeBlackWhiteLine(final String s) {
        for (int i = 0; i < s.length(); i++) {
            writeBlackWhite(s.charAt(i));
        }
        nextLine();
    }

    protected void writeBeforeBottomRow(final String s) {
        gameWindow.moveCursorToRowBeforeLast();
        writeBlackWhiteString(s);
    }

    protected void writeBottomRow(final String s) {
        gameWindow.moveCursorToLastRow();
        writeBlackWhiteString(s);
    }

    protected void fillLine(final char c) {
        gameWindow.fillCurrentRow(c);
    }

    protected String actionRow(final Map<Character, String> actions) {
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, String> action : actions.entrySet()) {
            sb.append("(" + action.getKey() + ") " + action.getValue() + "  ");
        }
        return sb.toString();
    }
}
