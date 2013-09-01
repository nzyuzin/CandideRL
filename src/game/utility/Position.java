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

import game.map.Map;

public final class Position {

	public final int x;
	public final int y;
	public final Map map;

	private Position(int x, int y, Map map) {
		this.x = x;
		this.y = y;
		this.map = map;
	}

	public static Position getPosition(int x, int y, Map map) {
		return new Position(x, y, map);
	}

	public double distanceTo(int x, int y) {
		return Math.sqrt((double) ((x - this.x) * (x - this.x))
				+ ((y - this.y) * (y - this.y)));
	}

	public double distanceTo(Position that) {
		return distanceTo(that.x, that.y);
	}

	public Position chooseClosest(Position first, Position second) {
		return this.distanceTo(first) < this.distanceTo(second) ? first
				: second;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
