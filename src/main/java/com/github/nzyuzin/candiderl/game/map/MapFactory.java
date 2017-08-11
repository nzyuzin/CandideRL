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

package com.github.nzyuzin.candiderl.game.map;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.GameConstants;
import com.github.nzyuzin.candiderl.game.characters.NpcFactory;
import com.github.nzyuzin.candiderl.game.map.generator.MapGenerator;
import com.google.common.io.LineReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapFactory {

    private final int width;
    private final int height;
    private final MapGenerator mapGenerator;
    private final NpcFactory npcFactory;

    public MapFactory(int width, int height, MapGenerator mapGenerator, NpcFactory npcFactory) {
        this.width = width;
        this.height = height;
        this.mapGenerator = mapGenerator;
        this.npcFactory = npcFactory;
    }

    public MapFactory(MapGenerator mapGenerator, NpcFactory npcFactory) {
        this(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT, mapGenerator, npcFactory);
    }

    public Map build() {
        final Map result;
        if (GameConfig.BUILD_MAP_FROM_FILE) {
            try {
                result = mapGenerator.generate(readMapFile());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read map file", e);
            }
        } else {
            result = mapGenerator.generate(width, height);
        }
        if (GameConfig.SPAWN_MOBS) {
            spawnMobs(result, 2);
        }
        return result;
    }

    private void spawnMobs(final Map map, final int number) {
        for (int i = 0; i < number; i++) {
            map.putGameCharacter(npcFactory.getNpc(), map.getRandomFreePosition());
        }
    }

    private char[][] readMapFile() throws IOException {
        File mapFile = new File(GameConstants.MAP_FILENAME);
        LineReader lineReader = new LineReader(new FileReader(mapFile));
        ArrayList<char[]> result = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            if (line.length() == 0) {
                break;
            }
            result.add(line.toCharArray());
        }
        return result.toArray(new char[result.size()][]);
    }

}
