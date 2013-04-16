package game.utility;

public final class DirectionProcessor {
	
	private DirectionProcessor() { }
	
	public static Position applyDirectionToPosition(Position pos, Direction there) {
		
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
	}
	
	public static Direction getDirectionFromPositions(Position from, Position to) {
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
	
	public static Direction getDirectionFromChar(char key) {

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