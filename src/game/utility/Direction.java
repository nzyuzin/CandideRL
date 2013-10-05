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

public enum Direction {
	NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
	
	public static Position applyDirection(Position pos, Direction there) {
		try {
		switch (there) {
		
		case NORTH:
			return Position.getPosition(pos.getX(), pos.getY() + 1);
		case SOUTH:
			return Position.getPosition(pos.getX(), pos.getY() - 1);
		case WEST:
			return Position.getPosition(pos.getX() - 1, pos.getY());
		case EAST:
			return Position.getPosition(pos.getX() + 1, pos.getY());
		case NORTHEAST:
			return Position.getPosition(pos.getX() + 1, pos.getY() + 1);
		case SOUTHEAST:
			return Position.getPosition(pos.getX() + 1, pos.getY() - 1);
		case SOUTHWEST:
			return Position.getPosition(pos.getX() - 1, pos.getY() - 1);
		case NORTHWEST:
			return Position.getPosition(pos.getX() - 1, pos.getY() + 1);
			
		default:
			return null;
			
		}
        } catch (AssertionError ex) {
            return pos;
        }
	}
	
	public static Direction getDirection(Position from, Position to) {
		if(from.getX() == to.getX() && from.getY() > to.getY())
			return Direction.SOUTH;
		if(from.getX() == to.getX() && from.getY() < to.getY())
			return Direction.NORTH;
		if(from.getX() > to.getX() && from.getY() == to.getY())
			return Direction.WEST;
		if(from.getX() < to.getX() && from.getY() == to.getY())
			return Direction.EAST;
		if(from.getX() > to.getX() && from.getY() < to.getY())
			return Direction.NORTHWEST;
		if(from.getX() < to.getX() && from.getY() < to.getY())
			return Direction.NORTHEAST;
		if(from.getX() > to.getX() && from.getY() > to.getY())
			return Direction.SOUTHWEST;
		if(from.getX() < to.getX() && from.getY() > to.getY())
			return Direction.SOUTHEAST;
		
		return null;
	}
	
	public static Direction getDirection(char key) {

		if(key == KeyDefinitions.DIRECTION_KEYS[0])
			return Direction.SOUTH;
		if(key == KeyDefinitions.DIRECTION_KEYS[1])
			return Direction.NORTH;
		if(key == KeyDefinitions.DIRECTION_KEYS[2])
			return Direction.WEST;
		if(key == KeyDefinitions.DIRECTION_KEYS[3])
			return Direction.EAST;
		if(key == KeyDefinitions.DIRECTION_KEYS[4])
			return Direction.NORTHWEST;
		if(key == KeyDefinitions.DIRECTION_KEYS[5])
			return Direction.NORTHEAST;
		if(key == KeyDefinitions.DIRECTION_KEYS[6])
			return Direction.SOUTHWEST;
		if(key == KeyDefinitions.DIRECTION_KEYS[7])
			return Direction.SOUTHEAST;

		return null;
	}
}
