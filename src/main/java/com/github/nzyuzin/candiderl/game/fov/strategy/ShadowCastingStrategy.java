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
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class ShadowCastingStrategy implements FovStrategy {

    public ShadowCastingStrategy() {
    }

    private boolean[][] seen;

    public boolean[][] calculateFov(boolean[][] transparent, int viewDistance) {
        Queue<Position> positionsQueue = new ArrayDeque<>();
        Queue<Direction> directionsQueue = new ArrayDeque<>();

        seen = new boolean[transparent.length][transparent[0].length];
        boolean[][] marked = new boolean[transparent.length][transparent[0].length];
        Direction[] directions = Direction.values();

        // watcher is supposed to be in center of seen array
        Position watcherPos = Position.getInstance(seen.length / 2, seen[0].length / 2);

        seen[watcherPos.getX()][watcherPos.getY()] = true;

        for (Direction direction : directions) {
            positionsQueue.add(watcherPos.apply(direction));
            directionsQueue.add(direction);
        }

        while (!positionsQueue.isEmpty()) {
            final Position pos = positionsQueue.poll();
            final Direction dir = directionsQueue.poll();
            final int x = pos.getX();
            final int y = pos.getY();

            if (!isInsideSeenArray(pos)
                    || watcherPos.distanceTo(pos) > viewDistance
                    || marked[x][y])
                continue;

            marked[x][y] = true;
            Position cellBetweenWatcher = pos.apply(pos.directionTo(watcherPos));
            seen[x][y] = seen[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()]
                    && transparent[cellBetweenWatcher.getX()][cellBetweenWatcher.getY()];

            if (seen[x][y] && transparent[x][y]) {
                for (final Direction newDir : getAdjacentDirections(dir)) {
                    Position newPosition = pos.apply(newDir);
                    if (newPosition != null) {
                        positionsQueue.add(newPosition);
                        directionsQueue.add(newDir);
                    }
                }
            }
        }
        return seen;
    }

    List<Direction> getAdjacentDirections(Direction dir) {
        switch (dir) {
            case NORTH: return Lists.newArrayList(Direction.NORTHWEST, Direction.NORTH, Direction.NORTHEAST);
            case NORTHEAST: return Lists.newArrayList(Direction.NORTH, Direction.NORTHEAST, Direction.EAST);
            case EAST: return Lists.newArrayList(Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST);
            case SOUTHEAST: return Lists.newArrayList(Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH);
            case SOUTH: return Lists.newArrayList(Direction.SOUTHEAST, Direction.SOUTH, Direction.SOUTHWEST);
            case SOUTHWEST: return Lists.newArrayList(Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST);
            case WEST: return Lists.newArrayList(Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST);
            case NORTHWEST: return Lists.newArrayList(Direction.WEST, Direction.NORTHWEST, Direction.NORTH);
        }
        throw new RuntimeException("Exhausted switch statement unexpected");
    }

    private boolean isInsideSeenArray(Position pos) {
        return isInsideSeenArray(pos.getX(), pos.getY());
    }

    private boolean isInsideSeenArray(int x, int y) {
        return x < seen.length && x >= 0 && y < seen[0].length && y >= 0;
    }
}
