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

package com.github.nzyuzin.candiderl.game.fov;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.fov.strategy.FovStrategy;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FieldOfVisionImpl implements FieldOfVision {
    private FovStrategy strategy;

    private static final Logger log = LoggerFactory.getLogger(FieldOfVisionImpl.class);

    /**
     * Builds field of view using specified strategy
     *
     * @param implementation FovStrategy to use, i.e. shadow casting
     */
    public FieldOfVisionImpl(FovStrategy implementation) {
        this.strategy = implementation;
    }

    public boolean isSeen(Position mapPos) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method converts field of view into printable array calculating it in process
     *
     * @return array of printable chars corresponding to watchers field of view
     */
    @Override
    public ColoredChar[][] getVisibleCells(int width, int height, GameCharacter watcher, int viewDistance) {
        long startTime = 0;
        if (log.isTraceEnabled()) {
            startTime = System.currentTimeMillis();
            log.trace("toColoredCharArray start");
        }
        Map map = watcher.getMap();
        if (!GameConfig.CALCULATE_FIELD_OF_VIEW) {
            return map.getVisibleChars(watcher.getPosition(), width, height);
        }
        // viewDistance * 2 + 1 stands here because view distance calculates
        // from watchers tile in every direction not including tile he's standing on
        // so we multiply by two for each side and add one for the watcher himself
        Boolean[][] seen = strategy.calculateFOV(map.getTransparentCells(
                watcher.getPosition(),
                viewDistance * 2 + 1,
                viewDistance * 2 + 1
        ), viewDistance);
        ColoredChar[][] resultMap =
                map.getVisibleChars(watcher.getPosition(), width, height);
        int xSeenOffset = resultMap.length / 2 - seen.length / 2;
        int ySeenOffset = resultMap[0].length / 2 - seen[0].length / 2;
        int xInSeen;
        int yInSeen;
        for (int x = 0; x < resultMap.length; x++) {
            for (int y = 0; y < resultMap[0].length; y++) {
                xInSeen = x - xSeenOffset;
                yInSeen = y - ySeenOffset;
                if (!isInsideArray(seen, xInSeen, yInSeen)
                        || seen[xInSeen][yInSeen] == null
                        || !seen[xInSeen][yInSeen])
                    resultMap[x][y] = ColoredChar.NIHIL;
            }
        }
        if (log.isTraceEnabled()) {
            log.trace(String.format("toColoredCharArray end :: %dms", System.currentTimeMillis() - startTime));
        }
        return resultMap;
    }

    private <T> boolean  isInsideArray(T[][] array, int x, int y) {
        return x >= 0 && y >= 0 && x < array.length && y < array[0].length;
    }

}
