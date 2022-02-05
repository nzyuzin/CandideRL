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

package com.github.nzyuzin.candiderl.game.utility;

import java.awt.Color;
import java.io.Serializable;

public final class ColoredChar implements Serializable {

    private final Color background;
    private final Color foreground;
    private final char visibleChar;

    public static final Color RED = Color.RED;
    public static final Color YELLOW = Color.YELLOW;
    public static final Color MAGENTA = Color.MAGENTA;
    public static final Color GREEN = Color.GREEN;
    public static final Color WHITE = Color.WHITE;
    public static final Color GRAY = Color.GRAY;
    public static final Color ORANGE = Color.ORANGE;

    public static final Color STANDARD_BACKGROUND_COLOR = Color.black;
    public static final Color STANDARD_FOREGROUND_COLOR = Color.white;

    public static final ColoredChar NIHIL = getColoredChar(' ');

    public static ColoredChar getColoredChar(char c, Color fg, Color bg) {
        return new ColoredChar(c, fg, bg);
    }

    public static ColoredChar getColoredChar(char c) {
        return getColoredChar(c, STANDARD_FOREGROUND_COLOR, STANDARD_BACKGROUND_COLOR);
    }

    public static ColoredChar getColoredChar(char c, Color fg) {
        return getColoredChar(c, fg, STANDARD_BACKGROUND_COLOR);
    }

    private ColoredChar(char c, Color fg, Color bg) {
        this.background = bg;
        this.foreground = fg;
        this.visibleChar = c;
    }

    public char getChar() {
        return this.visibleChar;
    }

    public Color getBackground() {
        return this.background;
    }

    public Color getForeground() {
        return this.foreground;
    }

    public String toString() {
        return String.format("Char: '%c', FG Color: '%s', BG Color: '%s'", visibleChar, foreground, background);
    }

    public static Color getColor(String rgb) {
        return Color.decode(rgb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColoredChar that = (ColoredChar) o;

        return background == that.background && foreground == that.foreground
                &&  visibleChar == that.visibleChar;

    }

    @Override
    public int hashCode() {
        int result = background.hashCode();
        result = 31 * result + foreground.hashCode();
        result = 31 * result + (int) visibleChar;
        return result;
    }
}
