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
			return new Position(pos.x, pos.y + 1);
		case SOUTH:
			return new Position(pos.x, pos.y - 1);
		case WEST:
			return new Position(pos.x - 1, pos.y);
		case EAST:
			return new Position(pos.x + 1, pos.y);
		case NORTHEAST:
			return new Position(pos.x + 1, pos.y + 1);
		case SOUTHEAST:
			return new Position(pos.x + 1, pos.y - 1);
		case SOUTHWEST:
			return new Position(pos.x - 1, pos.y - 1);
		case NORTHWEST:
			return new Position(pos.x - 1, pos.y + 1);
			
		default:
			return null;
			
		}
        } catch (AssertionError ex) {
            return pos;
        }
	}
	
	public static Direction getDirection(Position from, Position to) {
		if( from.x == to.x && from.y > to.y  )
			return Direction.SOUTH;
		if( from.x == to.x && from.y < to.y )
			return Direction.NORTH;
		if( from.x > to.x && from.y == to.y )
			return Direction.WEST;
		if( from.x < to.x && from.y == to.y )
			return Direction.EAST;
		if( from.x > to.x && from.y < to.y )
			return Direction.NORTHWEST;
		if( from.x < to.x && from.y < to.y )
			return Direction.NORTHEAST;
		if( from.x > to.x && from.y > to.y )
			return Direction.SOUTHWEST;
		if( from.x < to.x && from.y > to.y )
			return Direction.SOUTHEAST;
		
		return null;
	}
	
	public static Direction getDirection(char key) {

		if( key == KeyDefinitions.DIRECTION_KEYS[0] )
			return Direction.SOUTH;
		if( key == KeyDefinitions.DIRECTION_KEYS[1] )
			return Direction.NORTH;
		if( key == KeyDefinitions.DIRECTION_KEYS[2] )
			return Direction.WEST;
		if( key == KeyDefinitions.DIRECTION_KEYS[3] )
			return Direction.EAST;
		if( key == KeyDefinitions.DIRECTION_KEYS[4] )
			return Direction.NORTHWEST;
		if( key == KeyDefinitions.DIRECTION_KEYS[5] )
			return Direction.NORTHEAST;
		if( key == KeyDefinitions.DIRECTION_KEYS[6] )
			return Direction.SOUTHWEST;
		if( key == KeyDefinitions.DIRECTION_KEYS[7] )
			return Direction.SOUTHEAST;

		return null;
	}
}
