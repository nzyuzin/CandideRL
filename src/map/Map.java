package map;
import movement.Direction;
import java.util.Random;
import creatures.Hero;

class CellIsTaken extends Exception {
	private static final long serialVersionUID = 8382L;

	public CellIsTaken(int x, int y) { super("Cannot move here, the cell is taken: (" + x + ", " + y + ")"); }
}

class MapIsNotInitialized extends Exception {
	private static final long serialVersionUID = 8381L;
	
	public MapIsNotInitialized() { }
}

public final class Map {
	private static int mapsize = 0;
	private static Containable[][] map;
	
	public Map(int size) {
		//TODO make complicated map generation
		mapsize = size;
		map = new Containable[size][size];
		for(int i = 1; i < size - 1; i++)
			for(int j = 1; j < size -1; j++)
				map[i][j] = new Floor();
		for(int i = 0; i < size; i++) {
			map[0][i] = new Wall();
			map[size - 1][i] = new Wall();
			map[i][0] = new Wall();
			map[i][size - 1] = new Wall();
		}
	}
	
	private static Containable getContent(int x, int y) {
		assert x < mapsize && x >= 0 &&
				y < mapsize && y >= 0;
		return map[x][y];
	}
	
	private static boolean isEmpty(int x,int y) {
		assert x < mapsize && x >= 0 &&
				y < mapsize && y >= 0;
		if(getContent(x, y).type == "floor") return true;
		return false;
	}
	
	private static void makeEmpty(int x, int y) {
		assert x < mapsize && x >= 0 &&
				y < mapsize && y >= 0;
		getContent(x, y).x = -1;
		getContent(x, y).y = -1;
		setContent(x, y, new Floor());
	}
	
	private static void setContent(int x, int y, Containable content) {
		assert x < mapsize && x >= 0 &&
				y < mapsize && y >= 0;
		content.x = x;
		content.y = y;
		map[x][y] = content;
	}
	
	private static int[] getCoordinatesFromDirection(Containable that, Direction there) {
		int[] coordinates = new int[2];
		switch (there) {
		case UP:
			coordinates[0] = that.x;
			coordinates[1] = that.y + 1;
			return coordinates;
		case DOWN:
			coordinates[0] = that.x;
			coordinates[1] = that.y - 1;
			return coordinates;
		case LEFT:
			coordinates[0] = that.x - 1;
			coordinates[1] = that.y;
			return coordinates;
		case RIGHT:
			coordinates[0] = that.x + 1;
			coordinates[1] = that.y;
			return coordinates;
		default:
			return coordinates;
		}
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
	
	public static void moveContent(Containable that,Direction there) {
		int[] coordinates = getCoordinatesFromDirection(that, there);
		makeEmpty(that.x, that.y);
		setContent(coordinates[0], coordinates[1], that);
	}
	
	public static char[][] mapToCharArray() {
		char[][] chararray = new char[mapsize][mapsize];
		for(int i = 0; i < mapsize; i++)
			for(int j = 0; j < mapsize; j++)
				chararray[i][j] = getContent(i, j).charOnMap;
		return chararray;
	}
	
	public static Hero spawnHero(String name) {
		Random rand = new Random();
		Hero John = new Hero(name);
		John.x = rand.nextInt(mapsize - 2) + 1;
		John.y = rand.nextInt(mapsize - 2) + 1;
		setContent(John.x, John.y, John);
		return John;
	}
}
