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

public final class MapFactory {

	private static final int DEFAULT_SIZE = 10;

	private int screenWidth;
	private int screenHeight;

    private int mapWidth = 80;
    private int mapHeight = 25;

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
        return new Map(mapWidth, mapHeight, screenWidth, screenHeight);
	}

}
