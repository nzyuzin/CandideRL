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

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.fov.strategy.FovStrategy;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.Position;

final class FieldOfVisionImpl implements FieldOfVision {
    private FovStrategy strategy;

    /**
     * Builds field of view using specified strategy
     *
     * @param implementation FovStrategy to use, i.e. shadow casting
     */
    public FieldOfVisionImpl(FovStrategy implementation) {
        this.strategy = implementation;
    }

    /**
     * Method converts field of view into printable array calculating it in process
     *
     * @return array of printable chars corresponding to watchers field of view
     */
    @Override
    public boolean[][] calculateSeenMask(GameCharacter watcher, int viewDistance) {
        final Position watcherPos = watcher.getPosition();
        final Map map = watcher.getMap();
        // viewDistance * 2 + 1 stands here because view distance calculates
        // from watchers tile in every direction not including tile he's standing on
        // so we multiply by two for each side and add one for the watcher himself
        return strategy.calculateFov(map.getTransparentCells(
                watcherPos,
                viewDistance * 2 + 1,
                viewDistance * 2 + 1
        ), viewDistance);
    }

}
