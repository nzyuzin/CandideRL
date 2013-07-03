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
