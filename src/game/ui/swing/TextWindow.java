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

package game.ui.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

import java.util.Arrays;

public class TextWindow extends JComponent {

    private static final Log log = LogFactory.getLog(TextWindow.class);

    private static final Color DEFAULT_FOREGROUND = Color.LIGHT_GRAY;
    private static final Color DEFAULT_BACKGROUND = Color.BLACK;
    private static final Font DEFAULT_FONT = new Font("DejaVu Sans Mono", Font.PLAIN, 16);
    private static final boolean FIT_TO_SCREEN = false;

    private TextWindowContent data;

    private int fontWidth;
    private int fontHeight;
    private int fontYOffset;

    private int cursorColumn = 0;
    private int cursorRow = 0;
    private Font mainFont = null;
    private Color currentForeground = DEFAULT_FOREGROUND;
    private Color currentBackground = DEFAULT_BACKGROUND;

    public static TextWindow getTextWindow(int columns, int rows, Dimension screenSize) {
        TextWindow instance = new TextWindow();
        instance.init(columns, rows, screenSize);
        return instance;
    }

    private TextWindow() { }

    private void init(int columns, int rows, Dimension screenSize) {
        data = TextWindowContent.getTextWindowContent(columns, rows);

        data.fillText(' ');
        data.fillBackground(DEFAULT_BACKGROUND);
        data.fillForeground(DEFAULT_FOREGROUND);

        mainFont = DEFAULT_FONT;

        FontRenderContext fontRenderContext = new FontRenderContext(mainFont.getTransform(), true, false);
        Rectangle2D charBounds = mainFont.getStringBounds("X", fontRenderContext);
        fontWidth = (int) charBounds.getWidth();
        fontHeight = (int) charBounds.getHeight();
        fontYOffset = -(int) charBounds.getMinY() - fontHeight;

        int windowWidth = data.getColumns() * fontWidth;

        // TODO: find out where did this magical constant come from and figure out how to get rid of it
        int windowHeight = data.getRows() * fontHeight - (fontYOffset + fontHeight) * 2;
        Dimension windowSize = new Dimension(windowWidth, windowHeight);

        if (FIT_TO_SCREEN) {
            // TODO: think about real implementation, current one is crap
            float k = ((float) screenSize.width / (float) windowWidth + screenSize.height / windowHeight) / 2;
            mainFont = mainFont.deriveFont(k * mainFont.getSize2D());
            charBounds = mainFont.getStringBounds("X", fontRenderContext);
            fontWidth = (int) charBounds.getWidth();
            fontHeight = (int) charBounds.getHeight();
            fontYOffset = -(int) charBounds.getMinY() - fontHeight;
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("fontWidth = %d fontHeight = %d fontYOffset = %d",
                    fontWidth, fontHeight, fontYOffset)
            );
            log.debug(String.format("screenSize = %s", windowSize));
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setMaximumSize(windowSize);

        setFont(mainFont);
        repaint();

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
        Graphics2D g = (Graphics2D) graphics;
        Color fgColor;
        Color bgColor;
        int row;
        int column1;
        int column2;

        for (row = 0; row < data.getRows(); row++) {
            for (column1 = 0, column2 = 0; column1 < data.getColumns(); column1 = column2) {

                fgColor = data.getForegroundAt(column1, row);
                bgColor = data.getBackgroundAt(column1, row);

                //  invariant holds that place between column1 and column2 is filled with same color

                column2 = column1 + 1;

                while (column2 < data.getColumns() && fgColor == data.getForegroundAt(column2, row)
                        && bgColor == data.getBackgroundAt(column2, row)) {
                    column2++;
                }

                g.setBackground(bgColor);
                g.clearRect(fontWidth * column1, (row - 1) * fontHeight, fontWidth * (column2 - column1), fontHeight);

                g.setColor(fgColor);

                g.drawChars(
                        data.getRow(row),
                        column1,
                        column2 - column1,
                        column1 * fontWidth,
                        row * fontHeight + fontYOffset
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

    public void write(String string) {
        for (int i = 0; i < string.length(); i++) {
            write(string.charAt(i));
        }
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

    private void fillArea(char c, Color fg, Color bg, int column, int row,
                         int width, int height) {
        data.fillArea(c, fg, bg, column, row, width, height);
        repaintArea(column, row, width, height);
    }

    private void clearArea(int column, int row, int width, int height) {
        data.fillArea(' ', currentForeground, currentBackground, column, row, width, height);
        repaintArea(0, 0, width, height);
    }

    private void moveCursor() {
        cursorColumn++;
        if (cursorColumn >= data.getColumns()) {
            cursorColumn = 0;
            cursorRow++;
        }
        if (cursorRow + 1 >= data.getRows()) {
            cursorRow = 0;
            cursorColumn = 0;
        }

    }

}

/**
 * Internal class to hold TextWindow data
 */

final class TextWindowContent {
    private int capacity = 0;
    private int rows;
    private int columns;
    private Color[][] background;
    private Color[][] foreground;
    private char[][] text;

    private TextWindowContent() { }

    static TextWindowContent getTextWindowContent(int columns, int rows) {
        TextWindowContent instance = new TextWindowContent();
        instance.init(columns, rows);
        return instance;
    }

    void fillText(char c) {
        for (char[] arr : text) {
            Arrays.fill(arr, c);
        }
    }

    void fillBackground(Color c) {
        for (Color[] arr : background) {
            Arrays.fill(arr, c);
        }
    }

    void fillForeground(Color c) {
        for (Color[] arr : foreground) {
            Arrays.fill(arr, c);
        }
    }

    private void init(int columns, int rows) {
        text = new char[rows][columns];
        background = new Color[rows][columns];
        foreground = new Color[rows][columns];
        ensureCapacity(rows, columns);
        this.rows = rows;
        this.columns = columns;
    }

    private void ensureCapacity(int minRows, int minColumns) {
        if (capacity >= minRows * minColumns)
            return;

        char[][] newText = new char[minRows][minColumns];
        Color[][] newBackground = new Color[minRows][minColumns];
        Color[][] newForeground = new Color[minRows][minColumns];

        for (int i = 0; i < minRows; i++)
            System.arraycopy(text[i], 0, newText[i], 0, minColumns);
        for (int i = 0; i < minRows; i++)
            System.arraycopy(foreground[i], 0, newForeground[i], 0, minColumns);
        for (int i = 0; i < minRows; i++)
            System.arraycopy(background[i], 0, newBackground[i], 0, minColumns);

        text = newText;
        foreground = newForeground;
        background = newBackground;
        capacity = minRows * minColumns;
    }

    void setDataAt(int column, int row, char c, Color fg, Color bg) {
        text[row][column] = c;
        foreground[row][column] = fg;
        background[row][column] = bg;
    }

    char getCharAt(int column, int row) {
        return text[row][column];
    }

    Color getForegroundAt(int column, int row) {
        return foreground[row][column];
    }

    Color getBackgroundAt(int column, int row) {
        return background[row][column];
    }

    int getColumns() {
        return columns;
    }

    int getRows() {
        return rows;
    }

    char[] getRow(int n) {
        return text[n];
    }

    void fillArea(char c, Color fg, Color bg, int column,
                  int row, int width, int height) {
        for (int q = Math.max(0, row); q < Math.min(row + height, rows); q++) {
            for (int p = Math.max(0, column); p < Math.min(column + width, columns); p++) {
                text[q][p] = c;
                foreground[q][p] = fg;
                background[q][p] = bg;
            }
        }
    }
}