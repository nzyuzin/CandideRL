package game.utility;

import jcurses.system.CharColor;

public class Color {

	private short background = 0;
	private short foreground = 0;
	private boolean highlight = false;

	public static final short BLACK = CharColor.BLACK;
	public static final short RED = CharColor.RED;
	public static final short GREEN = CharColor.GREEN;
	public static final short YELLOW = CharColor.YELLOW;
	public static final short BLUE = CharColor.BLUE;
	public static final short MAGENTA = CharColor.MAGENTA;
	public static final short CYAN = CharColor.CYAN;
	public static final short WHITE = CharColor.WHITE;
	
	public static final short STANDART_BACKGROUND_COLOR = BLACK;
	public static final short STANDART_FOREGROUND_COLOR = WHITE;

	public Color () {
		this.background = STANDART_BACKGROUND_COLOR;
		this.foreground = STANDART_FOREGROUND_COLOR;
		this.highlight = false;
	}
	
	public Color (short fg) {
		this.foreground = fg;
		this.background = STANDART_BACKGROUND_COLOR;
		this.highlight = false;
	}
	
	public Color( short bg, short fg ) {
		this.background = bg;
		this.foreground = fg;
		this.highlight = false;
	}

	public Color( short bg, short fg, boolean highlight ) {
		this.background = bg;
		this.foreground = fg;
		this.highlight = highlight;
	}

	public Color( Color c ) {
		this.background = c.background;
		this.foreground = c.foreground;
		this.highlight = c.highlight;
	}

	public CharColor getCharColor() {
		CharColor c = new CharColor(this.background, this.foreground );
		if( this.highlight )
			c.setColorAttribute( CharColor.BOLD );

		return c;
	}
	
	public short getBackground() {
		return this.background;
	}
	
	public short getForeground() {
		return this.foreground;
	}

	public boolean equals( Color col ) {
		return( this.background == col.background &&
				this.foreground == col.foreground &&
				this.highlight == col.highlight );
	}

}
