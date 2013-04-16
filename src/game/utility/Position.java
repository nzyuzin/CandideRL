package game.utility;

import java.lang.Math;

public final class Position {
	public final int x;
	public final int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int distanceTo(Position target) {
		return (int) Math.sqrt((this.x - target.x) * (this.x - target.x) + (this.y - target.y) * (this.y - target.y));
	}
	
}