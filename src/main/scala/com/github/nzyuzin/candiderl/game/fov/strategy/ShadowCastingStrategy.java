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

package com.github.nzyuzin.candiderl.game.fov.strategy;

import com.github.nzyuzin.candiderl.game.utility.Direction;
import com.github.nzyuzin.candiderl.game.utility.Position;

import java.util.ArrayDeque;
import java.util.Queue;

public class ShadowCastingStrategy implements FovStrategy {

    public ShadowCastingStrategy() {
    }

    private Boolean[][] seen;

    public Boolean[][] calculateFOV(boolean[][] transparent, int viewDistance) {

        Queue<Position> positionsQueue = new ArrayDeque<>();

        Position pos;

        seen = new Boolean[transparent.length][transparent[0].length];
        boolean[][] marked = new boolean[transparent.length][transparent[0].length];
        Direction[] directions = Direction.values();

        // watcher is supposed to be in center of seen array
        Position watcherPos = Position.getInstance(seen.length / 2, seen[0].length / 2);

        seen[watcherPos.getX()][watcherPos.getY()] = true;

        for (int i = 0; i < directions.length; i++) {
            positionsQueue.add(watcherPos.apply(directions[i]));

            while (!positionsQueue.isEmpty()) {
                pos = positionsQueue.poll();

                if (!isInsideSeenArray(pos)
                        || watcherPos.distanceTo(pos) > viewDistance
                        || marked[pos.getX()][pos.getY()])
                    continue;

                marked[pos.getX()][pos.getY()] = true;
                Position cellBetweenWatcher = pos.apply(pos.directionTo(watcherPos));
                seen[pos.getX()][pos.getY()] = !(seen[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()] != null
                        && !seen[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()]
                        && !transparent[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()]);

                if (transparent[pos.getX()][pos.getY()]) {
                    for (int j = i - 1 + directions.length; j <= i + 1 + directions.length; j++) {
                        Position newPosition = pos.apply(directions[j % directions.length]);
                        if (newPosition != null) {
                            positionsQueue.add(newPosition);
                        }
                    }
                } else {
                    while (true) {
                        pos = pos.apply(watcherPos.directionTo(pos));
                        if (pos == null) {
                            break;
                        }
                        if (!isInsideSeenArray(pos)) {
                            break;
                        }
                        cellBetweenWatcher = pos.apply(pos.directionTo(watcherPos));
                        if (seen[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()] != null
                                && !seen[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()]
                                || !transparent[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()]) {
                            seen[pos.getX()][pos.getY()] = false;
                        }
                        marked[pos.getX()][pos.getY()] = true;
                    }
                }
            }
        }

        return seen;
    }

    private boolean isInsideSeenArray(Position pos) {
        return isInsideSeenArray(pos.getX(), pos.getY());
    }

    private boolean isInsideSeenArray(int x, int y) {
        return x < seen.length && x >= 0 && y < seen[0].length && y >= 0;
    }
}
