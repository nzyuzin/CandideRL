package game.ai;

import game.utility.DirectionProcessor;
import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.ArrayDeque;
import java.util.Queue;

class PathFinder {
	private static boolean[][] passable;
	private static boolean[][] checked;
	private static int[][] cost;
	private static Queue<Position> nodes = new ArrayDeque<Position>();
	
	public static void init(Position target) {
		makeMap(Map.toBooleanArray());
		bfs(target.x, target.y, 0);
	}
	
	public static Direction chooseQuickestWay(Position from) {
		int bestX = from.x;
		int bestY = from.y;
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++)
				if (	insideArray(x, y) &&
						passable[x][y] != false &&
						cost[x][y] < cost[bestX][bestY]) {
					bestX = x;
					bestY = y;
				}
		
		return DirectionProcessor.getDirectionFromPositions(from, new Position(bestX, bestY));
	}
	
	private static void bfs(int x, int y, int distance) {
		if (checked[x][y])
			return;
		
		checked[x][y] = true;
		
		if (!passable[x][y])
			cost[x][y] = 999;
		else
			cost[x][y] = distance;
		
		for (int i = x - 1; i <= x + 1; i++)
			for (int j = y - 1; j <= y + 1; j++)
				if ( insideArray(x, y) && i != x && j != y )
					nodes.add(new Position(x, y));
		
		Position next = nodes.poll();
		bfs( next.x, next.y, findLeastCost(next.x, next.y) + 1 );
	}
	
	private static int findLeastCost(int x, int y) {
		int leastCost = cost[x][y];
		
		for (int i = x - 1; i <= x + 1; i++)
			for (int j = y - 1; j <= y + 1; j++)
				if ( insideArray(x, y) &&
						leastCost > cost[i][j])
					leastCost = cost[i][j];
		return leastCost;
	}
	
	private static boolean insideArray(int x, int y) {
		if (x >= cost.length || x < 0 || y >= cost[0].length || y < 0)
			return false;
		return true;
	}
	
	private static void makeMap(boolean[][] map) {
		cost = new int[map.length][map[0].length];
		passable = map;
		checked = new boolean[map.length][map[0].length];
	}
	
}
