/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game.ai;

import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.Position;

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
     *
     * @param target position to calculate distances from
     */
    private void calculateDistances(Position target) {
        Queue<Position> positionsToProcess = new ArrayDeque<>();

        distance = new int[map.getWidth()][map.getHeight()];
        boolean[][] marked = new boolean[distance.length][distance[0].length];

        for (int i = 0; i < distance.length; i++)
            for (int j = 0; j < distance[0].length; j++)
                distance[i][j] = distanceLimit; // Initializing array of distances with maximal value

        marked[target.getX()][target.getY()] = true;
        distance[target.getX()][target.getY()] = 0;

        while (target != null) {

            if (distance[target.getX()][target.getY()] >= distanceLimit) {
                target = positionsToProcess.poll();
                continue;
            }

            for (int i = target.getX() - 1; i <= target.getX() + 1; i++)
                for (int j = target.getY() - 1; j <= target.getY() + 1; j++)
                    if (insideArray(i, j) && !marked[i][j]) {
                        Position p = Position.getInstance(i, j);
                        if (map.getCell(p).isPassable() && map.getCell(p).getGameCharacter() == null) {
                            positionsToProcess.add(p);
                            distance[i][j] = distance[target.getX()][target.getY()] + 1;
                            marked[i][j] = true;
                        }
                    }

            target = positionsToProcess.poll();
        }
    }

    public Position findNextMove(Position from) {
        Position best = from;
        Position p;
        for (int x = from.getX() - 1; x <= from.getX() + 1; x++)
            for (int y = from.getY() - 1; y <= from.getY() + 1; y++) {

                if (!insideArray(x, y)) {
                    continue;
                }

                p = Position.getInstance(x, y);

                if (map.getCell(p).getGameCharacter() == null && (distance[x][y] < distance[best.getX()][best.getY()]
                        || distance[x][y] == distance[best.getX()][best.getY()]
                        && p == target.closestBetweenTwo(p, best) && distance[x][y] != distanceLimit)) {
                    best = p;
                }
            }
        return best;
    }

    private boolean insideArray(int x, int y) {
        return x < distance.length && x >= 0 && y < distance[0].length && y >= 0;
    }

}
