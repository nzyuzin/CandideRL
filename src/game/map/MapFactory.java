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

package game.map;

import game.GameConfig;

public final class MapFactory {

    private int screenWidth;
    private int screenHeight;

    private int mapWidth = GameConfig.DEFAULT_MAP_SIZE;
    private int mapHeight = GameConfig.DEFAULT_MAP_SIZE;

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public static MapFactory getInstance() {
        return new MapFactory();
    }

    public void setMapSize(int width, int height) {
        mapWidth = width;
        mapHeight = height;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public Map getMap() {
        if (GameConfig.RANDOM_MAP)
            return Map.buildRandomizedMap(mapWidth, mapHeight, screenWidth, screenHeight, 0.25);
        return getEmptyMap();
    }

    public Map getEmptyMap() {
        return Map.buildEmptyMap(mapWidth, mapHeight, screenWidth, screenHeight);
    }

}
