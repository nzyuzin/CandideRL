package map;

import movement.Direction;
import java.util.Random;
import creatures.*;
import map.exceptions.*;

public final class Map {
	private static int mapWidth;
	private static int mapHeigth;


	private static MapCell[][] map;
	
	public Map(int height, int width) {
		//TODO make complicated map generation
		mapWidth = width;
		mapHeigth = height;
		
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
				y < mapHeigth && y >= 0;
		return map[x][y];
	}
	
	private static boolean isEmpty(int x,int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeigth && y >= 0;
		if(getCell(x, y).creature == null) return true;
		return false;
	}
	
	private static void removeCreature(int x, int y) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeigth && y >= 0;
		MapCell cell = getCell(x, y);
		cell.creature.posX = -1;
		cell.creature.posY = -1;
		cell.creature = null;
		cell.chooseCharOnMap();
	}
	
	private static void putCreature(int x, int y, Creature mob) {
		assert x < mapWidth && x >= 0 &&
				y < mapHeigth && y >= 0 &&
				mob.posX == -1 && mob.posY == -1;
		MapCell cell = getCell(x, y);
		mob.posX = x;
		mob.posY = y;
		cell.creature = mob;
		cell.chooseCharOnMap();
	}
	
	private static int[] applyDirectionToCoordinats(int x, int y, Direction there) {
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
			return coordinates;
		}
	}
	
//	public static MapCell getCell(Creature mob, Direction there) {
//		int[] coordinates = applyDirectionToCoordinats(mob.posX, mob.posY, there);
//		return getCell(coordinates[0], coordinates[1]);
//	}
	
	public static boolean canMoveCreature(Creature mob, Direction there) {
		int[] coordinates = applyDirectionToCoordinats(mob.posX, mob.posY, there);
		return getCell(coordinates[0], coordinates[1]).canBePassed;
	}
	
	public static void moveCreature(Creature mob, Direction there) throws CellIsTaken {
		int[] coordinates = applyDirectionToCoordinats(mob.posX, mob.posY, there);
		if(isEmpty(coordinates[0], coordinates[1])) {
			removeCreature(mob.posX, mob.posY);
			putCreature(coordinates[0], coordinates[1], mob);
		}
		else throw new CellIsTaken(getCell(coordinates[0],coordinates[1]).creature);
	}

	public static boolean canHit(Creature mob, Direction there) {
		int[] coordinates = applyDirectionToCoordinats(mob.posX, mob.posY, there);
		return getCell(coordinates[0], coordinates[1]).creature != null;
	}
	
	public static Creature getCreature(Creature mob, Direction there) {
		int[] coordinates = applyDirectionToCoordinats(mob.posX, mob.posY, there);
		return getCell(coordinates[0], coordinates[1]).creature;
	}
	
	public static String[] toStringArray() {
		String[] output = new String[mapHeigth];
		for(int i = 0; i < mapHeigth; i++)
			output[i] = "";
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeigth; j++)
				output[j] += getCell(i, j).visibleChar;
		}
		return output;
	}
	
	public static Hero spawnHero(String name) {
		Random rand = new Random();
		Hero John = new Hero(name);
		int posX = rand.nextInt(mapWidth - 2) + 1;
		int posY = rand.nextInt(mapHeigth - 2) + 1;
		putCreature(posX, posY, John);
		return John;
	}
}
