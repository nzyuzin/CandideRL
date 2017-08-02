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

package com.github.nzyuzin.candiderl.ui.swing;

import com.github.nzyuzin.candiderl.game.GameConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class TextWindow extends JComponent {

    private static final Logger log = LoggerFactory.getLogger(TextWindow.class);

    private static final Color DEFAULT_FOREGROUND = Color.LIGHT_GRAY;
    private static final Color DEFAULT_BACKGROUND = Color.BLACK;
    private TextWindowContent data;

    private int fontWidth;
    private int fontHeight;
    private int fontYOffset;

    private int cursorColumn = 0;
    private int cursorRow = 0;
    private Color currentForeground = DEFAULT_FOREGROUND;
    private Color currentBackground = DEFAULT_BACKGROUND;

    public static TextWindow getTextWindow(int columns, int rows) {
        return new TextWindow(columns, rows);
    }

    private TextWindow(int columns, int rows) {
        data = new TextWindowContent(columns, rows);
    }

    public void init() {
        data.init();
        data.fillText(' ');
        data.fillBackground(DEFAULT_BACKGROUND);
        data.fillForeground(DEFAULT_FOREGROUND);

        Font mainFont = GameConfig.DEFAULT_FONT;
        FontRenderContext fontRenderContext = new FontRenderContext(mainFont.getTransform(), true, false);
        Rectangle2D charBounds = mainFont.getStringBounds("X", fontRenderContext);
        fontWidth = (int) charBounds.getWidth();
        fontHeight = (int) charBounds.getHeight();
        fontYOffset = -(int) charBounds.getMinY() - fontHeight;
        final int windowWidth = data.getColumns() * fontWidth;
        final int windowHeight = (data.getRows() + 1) * fontHeight; // +1 is for the last row, we write below it
        Dimension windowSize = new Dimension(windowWidth, windowHeight);
        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setMaximumSize(windowSize);
        setFont(mainFont);
    }

    public int getFontWidth() {
        return fontWidth;
    }

    public int getFontHeight() {
        return fontHeight;
    }

    public void repaintArea(int column, int row, int width, int height) {
        int fw = getFontWidth();
        int fh = getFontHeight();
        repaint(column * fw, row * fh, width * fw, height * fh);
    }

    public void clear() {
        clearArea(0, 0, data.getColumns(), data.getRows());
    }

    public void resetCursor() {
        cursorColumn = 0;
        cursorRow = 0;
    }

    public void clearScreen() {
        clear();
        resetCursor();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int row = 0; row < data.getRows(); row++) {
            for (int col1 = 0, col2 = col1 + 1; col1 < data.getColumns(); col1 = col2, col2 = col1 + 1) {
                Color fgColor = data.getForegroundAt(col1, row);
                Color bgColor = data.getBackgroundAt(col1, row);
                while (col2 < data.getColumns() && fgColor == data.getForegroundAt(col2, row)
                        && bgColor == data.getBackgroundAt(col2, row)) {
                    col2++;
                }
                //  place between col1 and col2 is filled with same color
                g.setBackground(bgColor);
                g.clearRect(fontWidth * col1, row * fontHeight, fontWidth * (col2 - col1), fontHeight);
                g.setColor(fgColor);
                // row + 1 because drawing system puts images above Y coordinate
                g.drawChars(
                        data.getRow(row),
                        col1,
                        col2 - col1,
                        col1 * fontWidth,
                        (row + 1) * fontHeight + fontYOffset
                );
            }
        }
    }

    public void setForeground(Color c) {
        currentForeground = c;
    }

    public void setBackground(Color c) {
        currentBackground = c;
    }

    public Color getForeground() {
        return currentForeground;
    }

    public Color getBackground() {
        return currentBackground;
    }

    public void write(char c) {
        data.setDataAt(cursorColumn, cursorRow, c, currentForeground,
                currentBackground);
        moveCursor();
    }

    public void write(char c, Color foreGround, Color backGround) {
        Color foreTemp = currentForeground;
        Color backTemp = currentBackground;
        setForeground(foreGround);
        setBackground(backGround);
        write(c);
        setForeground(foreTemp);
        setBackground(backTemp);
    }

    private void clearArea(int column, int row, int width, int height) {
        data.fillArea(' ', currentForeground, currentBackground, column, row, width, height);
        repaintArea(0, 0, width, height);
    }

    public void moveCursor(int n) {
        for (int i = 0; i < n; i++) {
            moveCursor();
        }
    }

    public void moveCursor() {
        cursorColumn++;
        if (cursorColumn >= data.getColumns()) {
            cursorColumn = 0;
            cursorRow++;
        }
        if (cursorRow >= data.getRows()) {
            cursorRow = 0;
            cursorColumn = 0;
        }
    }

    public void moveCursorToLastRow() {
        resetCursor();
        moveCursor(getColumns() * (getRows() - 1));
    }

    public void nextLine() {
        cursorColumn = data.getColumns();
        moveCursor();
    }

    public int getColumns() {
        return data.getColumns();
    }

    public int getRows() {
        return data.getRows();
    }
}

