package game.ai;

import game.utility.DirectionProcessor;
import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.ArrayDeque;
import java.util.Queue;

public class PathFinder {
	private static boolean[][] passable;
	private static int[][] distance;
	private static Queue<Position> positionsToProcess = new ArrayDeque<Position>();
	private static int distanceLimit;
	
	public static void init(Position target, int distLimit) {
		
		distanceLimit = distLimit;
		updatePassable();
		makeMap();
		
		bfs(target, 0);
		
	}
	
	public static Direction chooseQuickestWay(Position from) {
		
		updatePassable();
		
		int bestX = from.x;
		int bestY = from.y;
		int okX = from.x;
		int okY = from.y;
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++) {
				
				if ( !insideArray(x, y) || !passable[x][y])
					continue;
				
				if ( distance[x][y] < distance[bestX][bestY] ) {
					bestX = x;
					bestY = y;
					continue;
				}
				
				if ( distance[x][y] == distance[bestX][bestY] ) {
					okX = x;
					okY = y;
				}
					
			}
		if (bestX != from.x || bestY != from.y)
			return DirectionProcessor.getDirectionFromPositions(from, new Position(bestX, bestY));
		return DirectionProcessor.getDirectionFromPositions(from, new Position(okX, okY));
	}
	
	private static void bfs(Position next, int dist) {
		
		boolean[][] checked = new boolean[passable.length][passable[0].length];
		
		checked[next.x][next.y] = true;

		distance[next.x][next.y] = dist;
		
		for (int i = next.x - 1; i <= next.x + 1; i++)
			for (int j = next.y - 1; j <= next.y + 1; j++)
				if ( insideArray(i, j) &&
						!checked[i][j] &&
						passable[i][j] ) {
					positionsToProcess.add(new Position(i, j));
					checked[i][j] = true;
				}
		
		next = positionsToProcess.poll();
		
		while (next != null) {
			
			dist = findLeastDistance(next, checked) + 1;

			if ( dist >= distanceLimit ) {
				next = positionsToProcess.poll();
				continue;
			}

			distance[next.x][next.y] = dist;

			for (int i = next.x - 1; i <= next.x + 1; i++)
				for (int j = next.y - 1; j <= next.y + 1; j++)
					if ( insideArray(i, j) &&
							!checked[i][j] &&
							passable[i][j] ) {
						positionsToProcess.add(new Position(i, j));
						checked[i][j] = true;
					}

			next = positionsToProcess.poll();
		}
	}
	
	private static int findLeastDistance(Position pos, boolean[][] checked) {
		int leastDistance = distanceLimit;
		
		for (int i = pos.x - 1; i <= pos.x + 1; i++)
			for (int j = pos.y - 1; j <= pos.y + 1; j++)
				if ( insideArray(i, j) && checked[i][j] &&  leastDistance > distance[i][j])
					leastDistance = distance[i][j];
		
		return leastDistance;
	}
	
	private static boolean insideArray(int x, int y) {
		return x < distance.length && x >= 0 && y < distance[0].length && y >= 0;
	}
	
	private static void updatePassable() {
		passable = Map.toBooleanArray();
	}
	
	private static void makeMap() {
		distance = new int[passable.length][passable[0].length];
		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance[0].length; j++)
				distance[i][j] = distanceLimit;
	}
	
}
