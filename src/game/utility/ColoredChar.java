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
	
	public static final short STANDART_BACKGROUND_COLOR = BLACK;
	public static final short STANDART_FOREGROUND_COLOR = CharColor.WHITE;
	
	public static final ColoredChar NIHIL = new ColoredChar(' ');

	public ColoredChar (char c) {
		this.background = STANDART_BACKGROUND_COLOR;
		this.foreground = STANDART_FOREGROUND_COLOR;
		this.highlight = false;
		this.visibleChar = c;
	}
	
	public ColoredChar (char c, short fg) {
		this.foreground = fg;
		this.background = STANDART_BACKGROUND_COLOR;
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
		CharColor c = new CharColor(this.background, this.foreground );
		if( this.highlight )
			c.setColorAttribute( CharColor.BOLD );

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
		return( this.background == col.background &&
				this.foreground == col.foreground &&
				this.highlight == col.highlight );
	}
	
	public String toString() {
		return visibleChar + "";
	}

}
