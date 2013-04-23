package game.map;

import game.characters.GameCharacter;
import game.utility.Position;
import game.utility.interfaces.GameItem;

import java.lang.StringBuffer;

public final class Map {
	
	private static int mapWidth;
	private static int mapHeight;
	
	private static int mapScreenHeight;
	private static int mapScreenWidth;

	private static MapCell[][] map;
	
	private Map() { }
	
	public static void init(int width, int height, int screenWidth, int screenHeight) {
		//TODO make complicated map generation
		
		mapWidth = width;
		mapHeight = height;
		
		mapScreenWidth = screenWidth;
		mapScreenHeight = screenHeight;
		
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
		
		for (int x = width / 2, uX = x + height / 2, uY = height / 2, dY = height / 2; x <= uX && dY < height && uY >= 0; x++, dY++, uY--, uX--)
			{
				map[x][uY] = wall;
				map[x][dY] = wall;
				map[uX][uY] = wall;
				map[uX][dY] = wall;
			}
		for (int x = width / 2 - 2, uX = x + height / 2 + 4, uY = height / 2, dY = height / 2; x <= uX && dY < height && uY >= 0; x++, dY++, uY--, uX--)
		{
			map[x][uY] = wall;
			map[x][dY] = wall;
			map[uX][uY] = wall;
			map[uX][dY] = wall;
		}
	}
	
	private static MapCell getCell(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return map[pos.x][pos.y];
	}
	
	private static char getVisibleChar(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeight && y >= 0;
		return map[x][y].getCharOnMap();
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
		
		for (int y = 0; y < mapHeight; y++)
			for (int x = 0; x < mapWidth; x++) {
				array[x][y] = map[x][y].canBePassed && map[x][y].gameCharacter == null;
			}
		
		return array;
	}
	
	public static String wholeMapAsString() {
		
		StringBuffer buffer = new StringBuffer();
		
		for (int y = mapHeight - 1; y >= 0; y--)
			for (int x = 0; x < mapWidth; x++)
				buffer.append(getVisibleChar(x, y));

		return buffer.toString();
	}
	
	public static String toOneString(Position pos) {
		
		StringBuffer buffer = new StringBuffer();
		
		int drawPosX = pos.x - (int) (mapScreenWidth / 2.0);
		int drawPosY = pos.y + (int) (mapScreenHeight / 2.0);

		for (int y = drawPosY - 1; y >= drawPosY - mapScreenHeight; y--)
			for (int x = drawPosX; x < drawPosX + mapScreenWidth; x++) {
				if ( x < mapWidth && x >= 0 && y < mapHeight && y >= 0)
					buffer.append(getVisibleChar(x, y));
				else buffer.append(" ");
			}

		return buffer.toString();
	}
	
	public static int getWidth() {
		return mapWidth;
	}
	
	public static int getHeight() {
		return mapHeight;
	}
	
}
