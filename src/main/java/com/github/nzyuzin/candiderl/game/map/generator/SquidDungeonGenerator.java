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
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import squidpony.squidgrid.mapping.ClassicRogueMapGenerator;

public class SquidDungeonGenerator extends AbstractMapGenerator {

    @Override
    public Map generate(int width, int height) {
        final ClassicRogueMapGenerator classicRogueMapGenerator =
                new ClassicRogueMapGenerator(3, 3, width, height, 2, 5, 2, 5);
        final Map map = MapFactory.build(classicRogueMapGenerator.generate());
        placeStairs(map);
        return map;
    }
}
