package game.utility;

import java.lang.Math;

public final class Position {
	public final int x;
	public final int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double distanceTo(Position target) {
		return Math.sqrt((this.x - target.x) * (this.x - target.x) + (this.y - target.y) * (this.y - target.y));
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public Position chooseClosest(Position first, Position second) {
		if (first.distanceTo(this) < second.distanceTo(this))
			return first;
		else 
			return second;
	}
	
}
