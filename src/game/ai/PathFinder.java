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
	
	Position target = null;
	
	public PathFinder(Position targetPos, int distLimit) {
		distanceLimit = distLimit;
		target = targetPos;
		calculateDistances(target);
	}
	
	/* 
	 * Fills distance array up using breadth-first-search 
	 * Takes initial position as argument
	 */
	
	private void calculateDistances(Position current) {
		
		// First element is point closest to initial
		Queue<Position> positionsToProcess = new ArrayDeque<Position>();
		
		// First element holds distance from initial point correspondingly to positionsToProcess queue
		Queue<Integer> positionDistances = new ArrayDeque<Integer>();
		
		distance = new int[Map.getWidth()][Map.getHeight()];
		boolean[][] checked = new boolean[distance.length][distance[0].length];
		
		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance[0].length; j++)
				distance[i][j] = distanceLimit; // Initializing array of distances with maximal value
	
		checked[current.x][current.y] = true;
		positionDistances.add(0);
		
		Position p;  // Only to simplify work with Positions inside the loop
		
		while (current != null) {

			if ( positionDistances.peek() >= distanceLimit ) {
				// No need to do something if it's too far
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

		Position best = from;
		
		Position p;
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++) {
				
				if ( !insideArray(x, y) )
					continue;
				
				p = new Position(x, y);
				
				if ( !Map.someoneHere(p) && (
						distance[x][y] < distance[best.x][best.y] || 
						( distance[x][y] == distance[best.x][best.y] && p == target.chooseClosest(p, best) ) ) )
					best = p;
			}
		
		return DirectionProcessor.getDirectionFromPositions(from, best);
		
	}
	
}
