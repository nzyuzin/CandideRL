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
import com.github.nzyuzin.candiderl.game.map.cells.Door;
import com.github.nzyuzin.candiderl.game.map.cells.Floor;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.map.cells.Wall;

public interface MapGenerator {

    Map generate(String name, int width, int height);

    default Map generate(String name, char[][] array) {
        final Map map = new Map(name, array[0].length, array.length);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                final char c = array[i][j];
                final MapCell cell = c == '#' ? Wall.getWall() : (c == '+' ? Door.getDoor() : Floor.getFloor());
                map.setCell(j, array.length - 1 - i, cell);
            }
        }
        return map;
    }

}
