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
import squidpony.squidgrid.mapping.DungeonGenerator;

public class SquidDungeonGenerator extends AbstractMapGenerator {

    @Override
    public Map generate(int width, int height) {
        final DungeonGenerator dungeonGenerator = new DungeonGenerator(width, height);
        dungeonGenerator.addDoors(20, true);
        final Map map = generate(dungeonGenerator.generate());
        System.out.println(dungeonGenerator.toString());
        placeBorder(map);
        placeStairs(map);
        return map;
    }
}
