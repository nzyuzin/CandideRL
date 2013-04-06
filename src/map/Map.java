package map;
import movement.Direction;

class CellIsTaken extends Exception {
	private static final long serialVersionUID = 17L;

	public CellIsTaken(int x, int y) { super("Cannot move here, the cell is taken: (" + x + ", " + y + ")"); }
}

class MapIsNotInitialized extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MapIsNotInitialized() { }
}

public final class Map {
	private static int mapsize = 0;
	private static Containable[][] map;
	private static char floor = '.', wall = '#';
	
	Map(int size) {
		//TODO make complicated map generation
		mapsize = size;
		map = new Containable[size][size];
		for(int i = 1; i < size - 1; i++)
			for(int j = 1; j < size -1; j++)
				map[i][j].charOnMap = floor;
		for(int i = 0; i < size; i++) {
			map[0][i].charOnMap = wall;
			map[size - 1][i].charOnMap = wall;
			map[i][0].charOnMap = wall;
			map[i][size - 1].charOnMap = wall;
		}
	}
	
	private static Containable getContent(int xcoor, int ycoor) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		return map[xcoor][ycoor];
	}
	
	private static boolean isEmpty(int xcoor,int ycoor) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		if(getContent(xcoor, ycoor).charOnMap == floor) return true;
		return false;
	}
	
	private static void makeEmpty(int xcoor, int ycoor) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		if(getContent(xcoor, ycoor).charOnMap != wall) {
			getContent(xcoor, ycoor).x = -1;
			getContent(xcoor, ycoor).y = -1;
			getContent(xcoor, ycoor).charOnMap = floor;
		}
	}
	
	private static void setContent(int xcoor, int ycoor, Containable content) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		content.x = xcoor;
		content.y = ycoor;
		map[xcoor][ycoor] = content;
	}
	
	private static int[] getCoordinatesFromDirection(Containable that, Direction there) {
		int[] coordinates = new int[1];
		if(there == Direction.UP) {
			coordinates[0] = that.x;
			coordinates[1] = that.y + 1;
			return coordinates;
		}		
		if(there == Direction.DOWN) {
			coordinates[0] = that.x;
			coordinates[1] = that.y - 1;
			return coordinates;
		}
		if(there == Direction.LEFT) {
			coordinates[0] = that.x - 1;
			coordinates[1] = that.y;
			return coordinates;
		}
		if(there == Direction.RIGHT) {
			coordinates[0] = that.x + 1;
			coordinates[1] = that.y;
			return coordinates;
		}
		return null;
	}
	
	public static Containable getContent(Containable that, Direction there) {
		int[] coordinates = getCoordinatesFromDirection(that, there);
		return getContent(coordinates[0], coordinates[1]);
	}
	
	public static boolean canMoveContent(Containable that, Direction there) {
		int[] coordinates = getCoordinatesFromDirection(that, there);
		if(isEmpty(coordinates[0], coordinates[1])) return true;
		return false;
	}
	
	public static void moveContent(Containable that,Direction there) throws CellIsTaken {
		int[] coordinates = getCoordinatesFromDirection(that, there);
		if(isEmpty(coordinates[0], coordinates[1])) {
			makeEmpty(coordinates[0], coordinates[1]);
			setContent(coordinates[0], coordinates[1], that);
			return;
		}
		else
			throw new CellIsTaken(coordinates[0], coordinates[1]);		
	}
	
	public static char[][] mapToCharArray() {
		char[][] chararray = new char[mapsize][mapsize];
		for(int i = 0; i < mapsize; i++)
			for(int j = 0; j < mapsize; j++)
				chararray[i][j] = getContent(i, j).charOnMap;
		return chararray;
	}
}
