package game.ai;

import game.utility.DirectionProcessor;
import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.ArrayDeque;
import java.util.Queue;

public class PathFinder {
	private int[][] distance;
	
	// BFS won't go on further distance than this limit
	private int distanceLimit;
	
	public PathFinder(Position target, int distLimit) {
		distanceLimit = distLimit;
		bfs(target);
	}
	
	private void bfs(Position current) {
		
		Queue<Position> positionsToProcess = new ArrayDeque<Position>();  // First element is closest to initial point
		Queue<Integer> positionDistances = new ArrayDeque<Integer>();     // First element holds distance from initial point of positionsToProcess queue
		distance = new int[Map.getWidth()][Map.getHeight()];
		boolean[][] checked = new boolean[distance.length][distance[0].length];
		
		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance[0].length; j++)
				distance[i][j] = distanceLimit; // Initializing array of distances with maximal value
	
		checked[current.x][current.y] = true;
		positionDistances.add(0);
		
		Position p;  // Only to simplify work with Positions inside the loop
		
		while (current != null) {

			if ( positionDistances.peek() >= distanceLimit ) { // No need to do something if it's too far
				current = positionsToProcess.poll();
				positionDistances.poll();
				continue;
			}

			distance[current.x][current.y] = positionDistances.poll();

			for (int i = current.x - 1; i <= current.x + 1; i++)
				for (int j = current.y - 1; j <= current.y + 1; j++)
					if ( insideArray(i, j) && !checked[i][j] ) {
						p = new Position(i, j);
						if ( Map.isCellPassable(p) ) {
							positionsToProcess.add(p);
							positionDistances.add(distance[current.x][current.y] + 1);
							checked[i][j] = true;
						}
					}

			current = positionsToProcess.poll();
		}
	}
	
	private boolean insideArray(int x, int y) {
		return x < distance.length && x >= 0 && y < distance[0].length && y >= 0;
	}
	
	public Direction chooseQuickestWay(Position from) {
		
		// bestX and bestY marks point with least distance, and okX, okY mark last point which has same cost as best.
		// okX and okY are needed to avoid situation in which no point has less distance than initial and so no movement is possible.
		// In other words, movement will be made unless all surrounding points have greater distance. 
		int bestX = from.x, bestY = from.y, okX = from.x, okY = from.y; 
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++) {
				
				if ( !insideArray(x, y) && Map.someoneHere(new Position(x, y)) )
					continue;
				
				if ( distance[x][y] < distance[bestX][bestY] ) {
					bestX = x;
					bestY = y;
					continue;
				}
				
				// Check for equality to initial point is vital in case when okPoint is found in first half of loop
				if ( distance[x][y] == distance[bestX][bestY] && (x != from.x || y != from.y) ) {
					okX = x;
					okY = y;
				}
					
			}
		
		// if bestPoint isn't initial use it. Otherwise okPoint (which can stay initial)
		if ( bestX != from.x || bestY != from.y )
			return DirectionProcessor.getDirectionFromPositions(from, new Position(bestX, bestY));
		else
			return DirectionProcessor.getDirectionFromPositions(from, new Position(okX, okY));
	}
	
}
