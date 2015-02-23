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

package com.github.nzyuzin.candiderl.game.map;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.GameConstants;
import com.google.common.io.LineReader;

import java.io.*;
import java.util.ArrayList;

public final class MapFactory {

    private int mapWidth = GameConfig.DEFAULT_MAP_SIZE;
    private int mapHeight = GameConfig.DEFAULT_MAP_SIZE;

    private MapFactory(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public static MapFactory getInstance(int mapWidth, int mapHeight) {
        return new MapFactory(mapWidth, mapHeight);
    }

    public Map getMap() {
        if (GameConfig.BUILD_MAP_FROM_FILE) {
            try {
                return getMapFrom(readMap());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read map file", e);
            }
        } else if (GameConfig.RANDOM_MAP) {
            return getRandomMap();
        } else {
            return getEmptyMap();
        }
    }

    public Map getEmptyMap() {
        return Map.buildEmptyMap(mapWidth, mapHeight);
    }

    public Map getRandomMap() {
        return Map.buildRandomizedMap(mapWidth, mapHeight, 0.25);
    }

    public static Map getMapFrom(char[][] array) {
        return Map.buildMapFrom(array);
    }

    private char[][] readMap() throws IOException {
        File mapFile = new File(GameConstants.MAP_FILENAME);
        LineReader lineReader = new LineReader(new FileReader(mapFile));
        ArrayList<char[]> result = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            result.add(line.replaceAll(System.lineSeparator(), "").toCharArray());
        }
        return result.toArray(new char[result.size()][]);
    }

}
