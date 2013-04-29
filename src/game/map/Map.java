package game.map;

import game.characters.GameCharacter;
import game.utility.Position;
import game.utility.interfaces.GameItem;
import game.utility.ColoredChar;

import java.lang.StringBuffer;

public final class Map {
	
	private static int mapWidth;
	private static int mapHeight;
	
	private static int mapHeightOnScreen;
	private static int mapWidthOnScreen;

	private static MapCell[][] map;
	
	private Map() { }
	
	public static void init(int width, int height, int screenWidth, int screenHeight) {
		//TODO make complicated map generation
		
		mapWidth = width;
		mapHeight = height;
		
		mapWidthOnScreen = screenWidth;
		mapHeightOnScreen = screenHeight;
		
		// Wall is of no use now, so it's meaningless to create more than one wall to fill space on map
		Wall wall = new Wall();
		map = new MapCell[width][height];
		
		for (int i = 1; i < width - 1; i++)
			for (int j = 1; j < height -1; j++)
				map[i][j] = new Floor();
		for (int i = 0; i < height; i++) {
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
		
		for ( int y = 0; y < height; y++) {
			map[width / 2 + 8][y] = wall;
		}
	}
	
	private static MapCell getCell(Position pos) {
		assert pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
		return map[pos.x][pos.y];
	}
	
	static ColoredChar getChar(Position pos) {
		return getCell(pos).getChar();
	}
	
	public static void removeGameCharacter(GameCharacter mob) {
		MapCell cell = getCell(mob.getPosition());
		mob.setPosition(null);
		cell.setGameCharacter(null);
	}
	
	public static void putGameCharacter(GameCharacter mob, Position pos) {
		getCell(pos).setGameCharacter(mob);
		mob.setPosition(pos);
	}
	
	public static void putItem(GameItem item, Position pos) {
		getCell(pos).putItem(item);
	}
	
	public static boolean isCellPassable(Position pos) {
		return getCell(pos).canBePassed;
	}
	
	public static boolean isCellTransparent(Position pos) {
		return getCell(pos).transparent;
	}
	
	public static void moveGameCharacter(GameCharacter mob, Position pos) {
		removeGameCharacter(mob);
		putGameCharacter(mob, pos);
	}

	public static boolean isSomeoneHere(Position pos) {
		return getCell(pos).gameCharacter != null;
	}
	
	public static GameCharacter getGameCharacter(Position pos) {
		return getCell(pos).gameCharacter;
	}
	
	public static int getPassageCost(Position pos) {
		return getCell(pos).passageCost;
	}
	
	public static Position getHeroPos(Position pos) {
		MapCell[][] partOfMap = getPartOfMap(pos, mapWidthOnScreen, mapHeightOnScreen);
		
		for (int y = partOfMap[0].length - 1; y >= 0; y--)
			for (int x = 0; x < partOfMap.length; x++)
				if ( partOfMap[x][y] != null && partOfMap[x][y].getGameCharacter() != null )
					return new Position(x, y);
		
		return new Position(0, 0);
	}
	
	public static String toString(Position pos) {
		
		MapCell[][] partOfMap = getPartOfMap(pos, mapWidthOnScreen, mapHeightOnScreen);
		
		StringBuffer buffer = new StringBuffer();

		for (int y = partOfMap[0].length - 1; y >= 0; y--)
			for (int x = 0; x < partOfMap.length; x++) {
				if ( partOfMap[x][y] != null )
					buffer.append( partOfMap[x][y].visibleChar );
				else buffer.append(" ");
			}

		return buffer.toString();
	}
	
	private static MapCell[][] getPartOfMap(Position pos, int width, int height) {
		return getPartOfMap(
				pos.x - (int) (width / 2.0),
				pos.x + (int) (width / 2.0),
				pos.y - (int) (height / 2.0),
				pos.y + (int) (height / 2.0));
	}
	
	static boolean[][] getTransparentCells(Position pos) {
		
		MapCell[][] partOfMap = getPartOfMap(pos, mapWidthOnScreen, mapHeightOnScreen);
		
		boolean[][] array = new boolean[partOfMap.length][partOfMap[0].length];
		
		for (int y = partOfMap[0].length - 1, i = 0; y >= 0; y--, i++)
			for (int x = 0, j = 0; x < partOfMap.length; x++, j++)
				if ( partOfMap[x][y] != null )
					array[j][i] = partOfMap[x][y].transparent;
		
		return array;
	}
	
	static ColoredChar[][] getVisibleChars(Position pos) {
		MapCell[][] partOfMap = getPartOfMap(pos, mapWidthOnScreen, mapHeightOnScreen);
		
		ColoredChar[][] result = new ColoredChar[partOfMap.length][partOfMap[0].length];
		
		for (int y = partOfMap[0].length - 1, i = 0; y >= 0; y--, i++)
			for (int x = 0, j = 0; x < partOfMap.length; x++, j++) {
				if ( partOfMap[x][y] != null )
					result[j][i] = partOfMap[x][y].visibleChar;
				else result[j][i] = ColoredChar.NIHIL;
			}
		
		return result;
	}
	
	private static MapCell[][] getPartOfMap(int lX, int uX, int lY, int uY) {
		MapCell[][] result = new MapCell[uX - lX][uY - lY];
		
		for (int x = lX, i = 0; x < uX; x++, i++)
			for (int y = lY, j = 0; y < uY; y++, j++) {
				if ( x < mapWidth && x >= 0 && y < mapHeight && y >= 0 )
					result[i][j] = map[x][y];
				else result[i][j] = null;
			}
		
		return result;
	}
	
	public static int getWidth() {
		return mapWidth;
	}
	
	public static int getHeight() {
		return mapHeight;
	}
	
	public static int getWidthOnScreen() {
		return mapWidthOnScreen;
	}
	
	public static int getHeightOnScreen() {
		return mapHeightOnScreen;
	}
	
	public static boolean insideMap(Position pos) {
		return pos.x < mapWidth && pos.x >= 0 &&
				pos.y < mapHeight && pos.y >= 0;
	}
	
	public static boolean insideMapScreen(Position pos) {
		return pos.x < mapWidthOnScreen && pos.x >= 0 &&
				pos.y < mapHeightOnScreen && pos.y >= 0;
	}
	
}
