package game.utility;

import java.awt.Point;

public final class Position {

	private final Point point;
	public final int x;
	public final int y;
	
	
	public Position(int x, int y) {
		this.point = new Point(x, y);
		this.x = x;
		this.y = y;
	}
	
	public Position(Position pos) {
		this.point = new Point(pos.x, pos.y);
		this.x = pos.x;
		this.y = pos.y;
	}
	
	public double distanceTo(Position that) {
		return point.distance(that.x, that.y);
	}
	
	public double distanceTo(int x, int y) {
		return point.distance(x, y);
	}
	
	public Position chooseClosest(Position first, Position second) {
		return this.distanceTo(first) < this.distanceTo(second) ? first : second; 
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
