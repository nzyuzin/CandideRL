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

    /**
     * Builds field of view using GameCharacter as watcher for whom field of view is calculated
     *
     * @param watcher GameCharacter for whom field of view is calculated
     * @param viewDistance watcher won't be able to see tiles further than this distance
     */
    public FieldOfView(GameCharacter watcher, int viewDistance) {
        this.viewDistance = viewDistance;
        this.watcher = watcher;
    }

    public boolean isSeen(Position mapPos) {
        // was never tested, probably bugged
        // intended to be used in future
        throw new UnsupportedOperationException();
//        int x = seen.length / 2 + mapPos.getX() - watcher.getPosition().getX();
//        int y = seen[0].length / 2 + mapPos.getY() - watcher.getPosition().getY();
//
//        return isInsideSeenArray(Position.getPosition(x, y)) && seen[x][y];
    }


    /**
     * Method converts field of view into printable array calculating it in process
     *
     * @return array of printable chars corresponding to watchers field of view
     */
    public ColoredChar[][] toColoredCharArray() {
        long startTime = 0;
        if (log.isTraceEnabled()) {
            startTime = System.currentTimeMillis();
            log.trace("toColoredCharArray start");
        }

        map = watcher.getPositionOnMap().getMap();

        if (!GameConfig.CALCULATE_FIELD_OF_VIEW) {
            return map.getVisibleChars(watcher.getPosition(), map.getWidthOnScreen(), map.getHeightOnScreen());
        }

        updateFOV();

        ColoredChar[][] resultMap =
                map.getVisibleChars(watcher.getPosition(), map.getWidthOnScreen(), map.getHeightOnScreen());

        // TODO: explain how it works
        int xSeenShift = resultMap.length / 2 - seen.length / 2;
        int ySeenShift = resultMap[0].length / 2 - seen[0].length / 2;

        for (int x = 0; x < resultMap.length; x++) {
            for (int y = 0; y < resultMap[0].length; y++) {
                if (!isInsideSeenArray(x - xSeenShift, y - ySeenShift) || !seen[x - xSeenShift][y - ySeenShift])
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

        // viewDistance * 2 + 1 stands here because view distance calculates
        // from watchers tile in every direction not including tile he's standing on
        // so we multiply by two for each side and add one for the watcher himself
        boolean[][] transparent = map.getTransparentCells(
                watcher.getPosition(),
                viewDistance * 2 + 1,
                viewDistance * 2 + 1
        );

        seen = new boolean[transparent.length][transparent[0].length];
        boolean[][] marked = new boolean[transparent.length][transparent[0].length];
        Direction[] directions = Direction.values();

        // watcher is supposed to be in center of seen array
        Position watcherPos = Position.getPosition(seen.length / 2, seen[0].length / 2);

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
                    for (int j = i - 1 + directions.length; j <= i + 1 + directions.length; j++) {
                        try {
                            positionsQueue.add(Direction
                                    .applyDirection(pos, directions[j % directions.length]));
                        } catch (IllegalArgumentException e) {
                            if (log.isErrorEnabled()) {
                                log.error(e);
                            }
                        }
                    }
                } else {
                    while (true) {
                        try {
                            pos = Direction.applyDirection(pos, directions[i]);
                        } catch (IllegalArgumentException e) {
                            break;
                        }
                        if (!isInsideSeenArray(pos))
                            break;
                        seen[pos.getX()][pos.getY()] = false;
                        marked[pos.getX()][pos.getY()] = true;
                    }
                }
            }
        }
    }

    private boolean isInsideSeenArray(Position pos) {
        return isInsideSeenArray(pos.getX(), pos.getY());
    }

    private boolean isInsideSeenArray(int x, int y) {
        return x < seen.length && x >= 0 && y < seen[0].length && y >= 0;
    }
}
