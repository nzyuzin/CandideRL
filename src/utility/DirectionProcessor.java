package utility;

public final class DirectionProcessor {
	
	private DirectionProcessor() { }
	
	public static int[] applyDirectionToCoordinats(int x, int y, Direction there) {
		
		int[] coordinates = new int[2];
		
		switch (there) {
		
		case NORTH:
			coordinates[0] = x;
			coordinates[1] = y + 1;
			return coordinates;
		case SOUTH:
			coordinates[0] = x;
			coordinates[1] = y - 1;
			return coordinates;
		case WEST:
			coordinates[0] = x - 1;
			coordinates[1] = y;
			return coordinates;
		case EAST:
			coordinates[0] = x + 1;
			coordinates[1] = y;
			return coordinates;
		case NORTHEAST:
			coordinates[0] = x + 1;
			coordinates[1] = y + 1;
			return coordinates;
		case SOUTHEAST:
			coordinates[0] = x + 1;
			coordinates[1] = y - 1;
			return coordinates;
		case SOUTHWEST:
			coordinates[0] = x - 1;
			coordinates[1] = y - 1;
			return coordinates;
		case NORTHWEST:
			coordinates[0] = x - 1;
			coordinates[1] = y + 1;
			return coordinates;
			
		default:
			return null;
			
		}
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