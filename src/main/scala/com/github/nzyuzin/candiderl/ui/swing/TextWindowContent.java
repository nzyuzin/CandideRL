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

import java.awt.Color;
import java.util.Arrays;

final class TextWindowContent {
    private int capacity = 0;
    private int rows;
    private int columns;
    private Color[][] background;
    private Color[][] foreground;
    private char[][] text;

    TextWindowContent(int columns, int rows) {
        text = new char[rows][columns];
        background = new Color[rows][columns];
        foreground = new Color[rows][columns];
        this.columns = columns;
        this.rows = rows;
    }

    public void init() {
        ensureCapacity(rows, columns);
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
