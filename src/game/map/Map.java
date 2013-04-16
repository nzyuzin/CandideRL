package game.map;

import game.characters.GameCharacter;
import game.utility.Position;
import game.utility.interfaces.GameItem;

import java.lang.StringBuffer;

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
			map[width / 2][i] = wall;
		}
		
		map[width / 2][height / 2] = new Floor();
		
		for (int i = 0; i < width; i++) {
			map[i][0] = wall;
			map[i][height - 1] = wall;
		}
	}
	
	private static MapCell getCell(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return map[pos.x][pos.y];
	}
	
	public static void removeGameCharacter(GameCharacter mob) {
		assert mob.getPosition().x < mapWidth && mob.getPosition().x >= 0 &&
				mob.getPosition().y < mapHeight && mob.getPosition().y >= 0;
		MapCell cell = getCell(mob.getPosition());
		mob.setPosition(null);
		cell.setGameCharacter(null);
	}
	
	public static void putGameCharacter(GameCharacter mob, Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		getCell(pos).setGameCharacter(mob);
		mob.setPosition(pos);
	}
	
	public static void putItem(GameItem item, Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		getCell(pos).putItem(item);
	}
	
	public static boolean isCellPassable(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return getCell(pos).canBePassed;
	}
	
	public static void moveGameCharacter(GameCharacter mob, Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		removeGameCharacter(mob);
		putGameCharacter(mob, pos);
	}

	public static boolean someoneHere(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return getCell(pos).gameCharacter != null;
	}
	
	public static GameCharacter getGameCharacter(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return getCell(pos).gameCharacter;
	}
	
	public static int getPassageCost(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return getCell(pos).passageCost;
	}
	
	public static boolean[][] toBooleanArray() {
		boolean[][] array = new boolean[mapWidth][mapHeight];
		for (int i = 0; i < mapHeight; i++) 
			for (int j = 0; j < mapWidth; j++)
				array[j][i] = map[j][i].canBePassed && map[j][i].gameCharacter == null;
		return array;
	}
	
	public static String[] toStringArray() {
		String[] output = new String[mapHeight];
		StringBuffer buffer = null;
		
		for (int i = 0; i < mapHeight; i++) {
			buffer = new StringBuffer();
			for (int j = 0; j < mapWidth; j++)
				buffer.append(map[j][i].visibleChar);
			output[i] = buffer.toString();
		}

		return output;
	}
}