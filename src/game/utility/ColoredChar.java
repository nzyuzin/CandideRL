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

import jcurses.system.CharColor;

public final class ColoredChar {

	private final short background;
	private final short foreground;
	private final boolean highlight;
	private final char visibleChar;

	public static final short BLACK = CharColor.BLACK;
	public static final short RED = CharColor.RED;
	public static final short GREEN = CharColor.GREEN;
	public static final short YELLOW = CharColor.YELLOW;
	public static final short BLUE = CharColor.BLUE;
	public static final short MAGENTA = CharColor.MAGENTA;
	public static final short CYAN = CharColor.CYAN;
	public static final short WHITE = CharColor.WHITE;
	
	public static final short STANDARD_BACKGROUND_COLOR = BLACK;
	public static final short STANDARD_FOREGROUND_COLOR = WHITE;
	
	public static final ColoredChar NIHIL = new ColoredChar(' ');

	public ColoredChar (char c) {
		this.background = STANDARD_BACKGROUND_COLOR;
		this.foreground = STANDARD_FOREGROUND_COLOR;
		this.highlight = false;
		this.visibleChar = c;
	}
	
	public ColoredChar (char c, short fg) {
		this.foreground = fg;
		this.background = STANDARD_BACKGROUND_COLOR;
		this.highlight = false;
		this.visibleChar = c;
	}
	
	public ColoredChar(char c, short bg, short fg) {
		this.background = bg;
		this.foreground = fg;
		this.highlight = false;
		this.visibleChar = c;
	}

	public ColoredChar(char c, short bg, short fg, boolean highlight) {
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

	public CharColor getColor() {
		CharColor c = new CharColor(this.background, this.foreground);
		if(this.highlight)
			c.setColorAttribute(CharColor.BOLD);

		return c;
	}
	
	public char getChar() {
		return this.visibleChar;
	}
	
	public short getBackground() {
		return this.background;
	}
	
	public short getForeground() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColoredChar that = (ColoredChar) o;

        if (background != that.background) return false;
        if (foreground != that.foreground) return false;
        if (highlight != that.highlight) return false;
        if (visibleChar != that.visibleChar) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) background;
        result = 31 * result + (int) foreground;
        result = 31 * result + (highlight ? 1 : 0);
        result = 31 * result + (int) visibleChar;
        return result;
    }
}
