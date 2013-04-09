package map;

import creatures.Creature;

public final class Map {
	private static int mapWidth;
	private static int mapHeight;


	private static MapCell[][] map;
	
	private Map() { }
	
	public static void init(int width, int height) {
		//TODO make complicated map generation
		
		mapWidth = width;
		mapHeight = height;
		
		// Wall is of no use now, so it's meaningless to create more than one wall to fill space on map
		Wall wall = new Wall();
		map = new MapCell[width][height];
		
		for(int i = 1; i < width - 1; i++)
			for(int j = 1; j < height -1; j++)
				map[i][j] = new Floor();
		for(int i = 0; i < height; i++) {
			map[0][i] = wall;
			map[width - 1][i] = wall;
		}
		for (int i = 0; i < width; i++) {
			map[i][0] = wall;
			map[i][height - 1] = wall;
		}
	}
	
	private static MapCell getCell(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		return map[x][y];
	}
	
	private static void removeCreature(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		MapCell cell = getCell(x, y);
		cell.creature = null;
		cell.chooseCharOnMap();
	}
	
	private static void putCreature(int x, int y, Creature mob) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		MapCell cell = getCell(x, y);
		cell.creature = mob;
		cell.chooseCharOnMap();
	}
	
	public static boolean isCellPassable(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		return getCell(x, y).canBePassed;
	}
	
	public static void moveCreature(Creature mob, int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		removeCreature(mob.posX, mob.posY);
		putCreature(x, y, mob);
	}

	public static boolean creatureHere(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		return getCell(x, y).creature != null;
	}
	
	public static Creature getCreature(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		return getCell(x, y).creature;
	}
	
	public static String[] toStringArray() {
		String[] output = new String[mapHeight];
		for(int i = 0; i < mapHeight; i++)
			output[i] = "";
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++)
				output[j] += getCell(i, j).visibleChar;
		}
		return output;
	}
}