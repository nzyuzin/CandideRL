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

package game.utility;

import java.awt.Color;

public final class ColoredChar {

	private final Color background;
	private final Color foreground;
	private final boolean highlight;
	private final char visibleChar;

    public static final Color RED = Color.RED;
    public static final Color YELLOW = Color.YELLOW;
    public static final Color MAGENTA = Color.MAGENTA;
    public static final Color GREEN = Color.GREEN;

	public static final Color STANDARD_BACKGROUND_COLOR = Color.black;
	public static final Color STANDARD_FOREGROUND_COLOR = Color.white;

	public static final ColoredChar NIHIL = new ColoredChar(' ');

	public ColoredChar (char c) {
		this.background = STANDARD_BACKGROUND_COLOR;
		this.foreground = STANDARD_FOREGROUND_COLOR;
		this.highlight = false;
		this.visibleChar = c;
	}

	public ColoredChar (char c, Color fg) {
		this.foreground = fg;
		this.background = STANDARD_BACKGROUND_COLOR;
		this.highlight = false;
		this.visibleChar = c;
	}

	public ColoredChar(char c, Color fg, Color bg) {
		this.background = bg;
		this.foreground = fg;
		this.highlight = false;
		this.visibleChar = c;
	}

	public ColoredChar(char c, Color fg, Color bg, boolean highlight) {
		this.background = bg;
		this.foreground = fg;
		this.highlight = highlight;
		this.visibleChar = c;
	}

	public ColoredChar( ColoredChar c ) {
		this.background = c.background;
		this.foreground = c.foreground;
		this.highlight = c.highlight;
		this.visibleChar = c.visibleChar;
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

	public boolean equals( ColoredChar col ) {
		return (this.background == col.background &&
				this.foreground == col.foreground &&
				this.highlight == col.highlight);
	}

	public String toString() {
		return visibleChar + "";
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
               && highlight == that.highlight && visibleChar == that.visibleChar;

    }

    @Override
    public int hashCode() {
        int result = background.hashCode();
        result = 31 * result + foreground.hashCode();
        result = 31 * result + (highlight ? 1 : 0);
        result = 31 * result + (int) visibleChar;
        return result;
    }
}
