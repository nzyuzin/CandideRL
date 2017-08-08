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

import com.github.nzyuzin.candiderl.game.characters.NpcFactory;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.map.cells.Wall;

public abstract class AbstractMapGenerator implements MapGenerator {

    private final NpcFactory npcFactory;

    public AbstractMapGenerator(NpcFactory npcFactory) {
        this.npcFactory = npcFactory;
    }

    protected void placeStairs(final Map map) {
        map.setCell(map.getRandomFreePosition(), new Stairs(Stairs.Type.DOWN));
        map.setCell(map.getRandomFreePosition(), new Stairs(Stairs.Type.UP));
    }

    protected void placeBorder(final Map map) {
        for (int i = 0; i < map.getHeight(); i++) {
            map.setCell(0, i, Wall.getWall());
            map.setCell(map.getWidth() - 1, i, Wall.getWall());
        }
        for (int i = 0; i < map.getWidth(); i++) {
            map.setCell(i, 0, Wall.getWall());
            map.setCell(i, map.getHeight() - 1, Wall.getWall());
        }
    }

    @Override
    public void spawnMobs(final Map map, final int number) {
        for (int i = 0; i < number; i++) {
            map.putGameCharacter(npcFactory.getNpc(), map.getRandomFreePosition());
        }
    }

}
