/*
 *  This file is part of CandideRL.
 *
 *  CandideRL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CandideRL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.ai;

import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.ArrayDeque;
import java.util.Queue;

public class PathFinder {
	private int[][] distance;

	// BFS won't go on further distance than this limit
	private int distanceLimit;
    private final Map map;

	Position target = null;

	public PathFinder(Position targetPos, int distLimit, Map map) {
		distanceLimit = distLimit;
		target = targetPos;
        this.map = map;
		calculateDistances(target);
	}

	/**
	 * Fills distance array up using breadth-first-search
	 * Takes initial position as argument
	 */
	private void calculateDistances(Position current) {
		Queue<Position> positionsToProcess = new ArrayDeque<>();

		distance = new int[map.getWidth()][map.getHeight()];
		boolean[][] checked = new boolean[distance.length][distance[0].length];

		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance[0].length; j++)
				distance[i][j] = distanceLimit; // Initializing array of distances with maximal value

		checked[current.getX()][current.getY()] = true;
		distance[current.getX()][current.getY()] = 0;

		Position p;

		while (current != null) {

			if ( distance[current.getX()][current.getY()] >= distanceLimit ) {
				current = positionsToProcess.poll();
				continue;
			}

			for (int i = current.getX() - 1; i <= current.getX() + 1; i++)
				for (int j = current.getY() - 1; j <= current.getY() + 1; j++)
					if ( insideArray(i, j) && !checked[i][j] ) {
						p = Position.getPosition(i, j);
						if (map.isCellPassable(p)) {
							positionsToProcess.add(p);
							distance[i][j] = distance[current.getX()][current.getY()] + 1;
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

		for (int x = from.getX() - 1; x <= from.getX() + 1; x++)
			for (int y = from.getY() - 1; y <= from.getY() + 1; y++) {

				if (!insideArray(x, y))
					continue;

				p = Position.getPosition(x, y);

				if (!map.isSomeoneHere(p)
                        && (distance[x][y] < distance[best.getX()][best.getY()]
                            || (distance[x][y] == distance[best.getX()][best.getY()]
                                 && p == target.chooseClosest(p, best))))
					best = p;
			}

		return Direction.getDirection(from, best);

	}

}
