package game.ai;

import game.utility.DirectionProcessor;
import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.ArrayDeque;
import java.util.Queue;

public class PathFinder {
	private static boolean[][] passable;
	private static boolean[][] checked;
	private static int[][] cost;
	private static Queue<Position> nodes = new ArrayDeque<Position>();
	
	public static void init(Position target) {
		makeMap(Map.toBooleanArray());
		checked[target.x][target.y] = true;
		bfs(target.x, target.y, 0);
	}
	
	public static String costToString() {
		String result = "";
		for (int i = cost.length - 1; i >= 0; i--)
			for (int j = 0; j < cost[0].length; j++)
				result += cost[i][j] + "";
		return result;
	}
	
	public static Direction chooseQuickestWay(Position from) {
		int bestX = from.x;
		int bestY = from.y;
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++)
				if (	insideArray(x, y) &&
						cost[x][y] < cost[bestX][bestY]) {
					bestX = x;
					bestY = y;
				}
		
		return DirectionProcessor.getDirectionFromPositions(from, new Position(bestX, bestY));
	}
	
	private static void bfs(int x, int y, int distance) {
		cost[x][y] = distance;
//		game.ui.GameUI.showMessage("( " + x + ", " + y + ")" );
		
		for (int i = x - 1; i <= x + 1; i++)
			for (int j = y - 1; j <= y + 1; j++)
				if ( insideArray(i, j) && !checked[i][j] && passable[i][j]) {
					nodes.add(new Position(i, j));
					checked[i][j] = true;
				}
		
		Position next = nodes.poll();

		if (!nodes.isEmpty())
			bfs( next.x, next.y, findLeastCost(next.x, next.y) + 1 );
	}
	
	private static int findLeastCost(int x, int y) {
		int leastCost = cost[x][y];
		
		for (int i = x - 1; i <= x + 1; i++)
			for (int j = y - 1; j <= y + 1; j++)
				if ( insideArray(i, j) &&  leastCost > cost[i][j])
					leastCost = cost[i][j];
		return leastCost;
	}
	
	private static boolean insideArray(int x, int y) {
		try {
			if (passable[x][y])
				return true;
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private static void makeMap(boolean[][] map) {
		cost = new int[map.length][map[0].length];
		for (int i = 0; i < cost.length; i++)
			for (int j = 0; j < cost[0].length; j++)
				cost[i][j] = 9;
		passable = map;
		checked = new boolean[map.length][map[0].length];
		for (int i = 0; i < checked.length; i++)
			for (int j = 0; j < checked[0].length; j++)
				checked[i][j] = false;
	}
	
}
