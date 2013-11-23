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

package game.map;

import game.GameConfig;
import game.characters.GameCharacter;
import game.utility.ColoredChar;
import game.utility.Direction;
import game.utility.Position;

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FieldOfView {
    private final GameCharacter watcher;
    private boolean[][] seen;
    private int viewDistance;
    private Map map;

    private static final Log log = LogFactory.getLog(FieldOfView.class);

    public FieldOfView(GameCharacter c, int viewDistance) {
        this.viewDistance = viewDistance;
        this.watcher = c;
    }

    public boolean isSeen(Position mapPos) {
        int x = seen.length + mapPos.getX() - watcher.getPosition().getX();
        int y = seen[0].length + mapPos.getY() - watcher.getPosition().getY();

        return isInsideSeenArray(Position.getPosition(x, y)) && seen[x][y];
    }

    public ColoredChar[][] toColoredCharArray() {
        long startTime = 0;
        if (log.isTraceEnabled()) {
            startTime = System.currentTimeMillis();
            log.trace("toColoredCharArray start");
        }

        map = watcher.getPositionOnMap().getMap();

        if (!GameConfig.CALCULATE_FIELD_OF_VIEW) {
            return map.getVisibleChars(watcher.getPosition());
        }

        updateFOV();

        ColoredChar[][] resultMap = map.getVisibleChars(watcher.getPosition());

        for (int x = 0; x < resultMap.length; x++) {
            for (int y = 0; y < resultMap[0].length; y++) {
                if (!seen[x][y])
                    resultMap[x][y] = ColoredChar.NIHIL;
            }
        }

        if (log.isTraceEnabled()) {
            log.trace(String.format("toColoredCharArray end :: %dms", System.currentTimeMillis() - startTime));
        }
        return resultMap;
    }

    private void updateFOV() {

        Queue<Position> positionsQueue = new ArrayDeque<>();

        Position pos;
        boolean[][] transparent = map.getTransparentCells(watcher.getPosition());
        seen = new boolean[transparent.length][transparent[0].length];
        boolean[][] marked = new boolean[transparent.length][transparent[0].length];
        Direction[] directions = Direction.values();

        // TODO: Why -1 ?
        Position watcherPos = Position.getPosition(seen.length / 2, seen[0].length / 2 - 1);

        seen[watcherPos.getX()][watcherPos.getY()] = true;

        for (int i = 0; i < directions.length; i++) {
            positionsQueue.add(Direction.applyDirection(watcherPos, directions[i]));

            while (!positionsQueue.isEmpty()) {
                pos = positionsQueue.poll();

                if (!isInsideSeenArray(pos)
                        || watcherPos.distanceTo(pos) > viewDistance
                        || marked[pos.getX()][pos.getY()])
                    continue;

                marked[pos.getX()][pos.getY()] = true;
                seen[pos.getX()][pos.getY()] = true;

                if (transparent[pos.getX()][pos.getY()]) {
                    for (int j = i - 1; j <= i + 1; j++) {
                        try {
                            positionsQueue.add(Direction
                                    .applyDirection(pos, directions[(j + directions.length)  % directions.length]));
                        } catch (IllegalArgumentException e) {
                            if (log.isErrorEnabled()) {
                                log.error(e);
                            }
                        }
                    }
                } else {
                    pos = Direction.applyDirection(pos, directions[i]);
                    while (isInsideSeenArray(pos)) {
                        seen[pos.getX()][pos.getY()] = false;
                        marked[pos.getX()][pos.getY()] = true;
                        try {
                            pos = Direction.applyDirection(pos, directions[i]);
                        } catch (IllegalArgumentException e) {
                            if (log.isErrorEnabled()) {
                                log.error(e);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isInsideSeenArray(Position pos) {
        return pos.getX() < seen.length && pos.getX() >= 0 && pos.getY() < seen[0].length && pos.getY() >= 0;
    }
}
