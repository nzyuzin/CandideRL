package game.ai;

import game.utility.DirectionProcessor;
import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.ArrayDeque;
import java.util.Queue;

public class PathFinder {
	private static int[][] distance;
	private static int distanceLimit;
	
	public static void init(Position target, int distLimit) {
		distanceLimit = distLimit;
		bfs(target);
	}
	
	public static Direction chooseQuickestWay(Position from) {
		
		int bestX = from.x;
		int bestY = from.y;
		int okX = from.x;
		int okY = from.y;
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++) {
				
				if ( !insideArray(x, y) )
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
		
		if ( bestX != from.x || bestY != from.y )
			return DirectionProcessor.getDirectionFromPositions(from, new Position(bestX, bestY));
		else
			return DirectionProcessor.getDirectionFromPositions(from, new Position(okX, okY));
	}
	
	private static void bfs(Position next) {
		
		Queue<Position> positionsToProcess = new ArrayDeque<Position>();
		Queue<Integer> positionDistances = new ArrayDeque<Integer>();
		boolean[][] checked = new boolean[distance.length][distance[0].length];
		
		distance = new int[Map.getWidth()][Map.getHeight()];
		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance[0].length; j++)
				distance[i][j] = distanceLimit;
		
		checked[next.x][next.y] = true;
		distance[next.x][next.y] = 0;
		
		Position p;  // Only to simplify work with Positions inside the loops
		
		for (int i = next.x - 1; i <= next.x + 1; i++)
			for (int j = next.y - 1; j <= next.y + 1; j++)
				if ( insideArray(i, j) && !checked[i][j] ) {
					p = new Position(i, j);
					if ( Map.isCellPassable(p) && !Map.someoneHere(p) ) {
						positionsToProcess.add(p);
						positionDistances.add(distance[next.x][next.y] + 1);
						checked[i][j] = true;
					}
				}
		
		next = positionsToProcess.poll();
		
		while (next != null) {

			if ( positionDistances.peek() >= distanceLimit ) {
				next = positionsToProcess.poll();
				positionDistances.poll();
				continue;
			}

			distance[next.x][next.y] = positionDistances.poll();

			for (int i = next.x - 1; i <= next.x + 1; i++)
				for (int j = next.y - 1; j <= next.y + 1; j++)
					if ( insideArray(i, j) && !checked[i][j] ) {
						p = new Position(i, j);
						if ( Map.isCellPassable(p) && !Map.someoneHere(p) ) {
							positionsToProcess.add(p);
							positionDistances.add(distance[next.x][next.y] + 1);
							checked[i][j] = true;
						}
					}

			next = positionsToProcess.poll();
		}
	}
	
	private static boolean insideArray(int x, int y) {
		return x < distance.length && x >= 0 && y < distance[0].length && y >= 0;
	}
	
}
