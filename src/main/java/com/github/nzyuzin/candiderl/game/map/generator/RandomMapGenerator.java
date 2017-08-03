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

package com.github.nzyuzin.candiderl.game.map.generator;

import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.map.cells.Wall;
import com.google.common.base.Preconditions;

public class RandomMapGenerator extends AbstractMapGenerator {

    private final double filledCells;
    private final EmptyMapGenerator emptyMapGenerator;

    public RandomMapGenerator(final double filledCells, final EmptyMapGenerator emptyMapGenerator) {
        this.filledCells = filledCells;
        this.emptyMapGenerator = emptyMapGenerator;
    }

    @Override
    public Map generate(final int width, final int height) {
        Preconditions.checkArgument(filledCells < 1 && filledCells > 0);
        final Map map = emptyMapGenerator.generate(width, height);
        for (int i = 0; i < height * width * filledCells; i++) {
            map.setCell(map.getRandomFreePosition(), Wall.getWall());
        }
        map.setCell(map.getRandomFreePosition(), new Stairs(Stairs.Type.DOWN));
        map.setCell(map.getRandomFreePosition(), new Stairs(Stairs.Type.UP));
        return map;
    }
}
