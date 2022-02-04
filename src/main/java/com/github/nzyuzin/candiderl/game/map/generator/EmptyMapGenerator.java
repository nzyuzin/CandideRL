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
import com.github.nzyuzin.candiderl.game.map.cells.Floor;
import com.google.common.base.Preconditions;

public class EmptyMapGenerator extends AbstractMapGenerator {

    @Override
    public Map generate(final String name, final int width, final int height) {
        Preconditions.checkArgument(width >= 3 && height >= 3, "Width and height should be at least 3!");
        final Map map = new Map(name, width, height);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                map.setCell(i, j, Floor.getFloor());
        placeBorder(map);
        placeStairs(map);
        return map;
    }
}
