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

import game.characters.GameCharacter;
import game.utility.ColoredChar;
import game.utility.Position;
import game.items.GameItem;
import game.utility.PositionOnMap;

import java.util.Random;
import java.util.Formatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Map {

    private final static Log log = LogFactory.getLog(Map.class);

    private final int mapWidth;
    private final int mapHeight;

    private final int mapHeightOnScreen;
    private final int mapWidthOnScreen;

    private final MapCell[][] map;

    private Map(int width, int height, int screenWidth, int screenHeight) {
        assert width > 0 && height > 0 && screenHeight > 0 && screenWidth > 0;

        map = new MapCell[width][height];

        mapWidth = width;
        mapHeight = height;

        mapWidthOnScreen = screenWidth;
        mapHeightOnScreen = screenHeight;
    }

    static Map buildEmptyMap(int width, int height, int screenWidth, int screenHeight) {
        Map instance = new Map(width, height, screenWidth, screenHeight);

        if (log.isTraceEnabled()) {
            log.trace(new Formatter().format("Map construction start :: passed arguments: %d %d %d %d%nTotal Map size = %d",
                    width, height, screenWidth, screenHeight, width * height));
        }


        // Wall is of no use now, so it's meaningless to create more
        // than one wall to fill space on map
        Wall wall = Wall.getWall();


        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                instance.setCell(Position.getPosition(i, j), Floor.getFloor());
        for (int i = 0; i < height; i++) {
            instance.setCell(Position.getPosition(0, i), wall);
            instance.setCell(Position.getPosition(width - 1, i), wall);
        }

        for (int i = 0; i < width; i++) {
            instance.setCell(Position.getPosition(i, 0), wall);
            instance.setCell(Position.getPosition(i, height - 1), wall);
        }

        if (log.isTraceEnabled()) {
            log.trace("Map construction end");
        }

        return instance;
    }

    static Map buildRandomizedMap(int width, int height, int screenWidth, int screenHeight, double filledCells) {

        assert filledCells < 1 && filledCells > 0;

        Map instance = buildEmptyMap(width, height, screenWidth, screenHeight);

        for (int i = 0; i < instance.mapHeight * instance.mapWidth * filledCells; i++) {
            instance.setCell(instance.getRandomFreePosition(), Wall.getWall());
        }

        return instance;
    }

    MapCell getCell(Position pos) {
        assert pos.getX() < mapWidth && pos.getX() >= 0 && pos.getY() < mapHeight
                && pos.getY() >= 0;
        return map[pos.getX()][pos.getY()];
    }

    void setCell(Position pos, MapCell cell) {
        assert pos.getX() < mapWidth && pos.getX() >= 0 && pos.getY() < mapHeight
                && pos.getY() >= 0;
        map[pos.getX()][pos.getY()] = cell;
    }

    ColoredChar getCellChar(Position pos) {
        return getCell(pos).getDefaultChar();
    }

    ColoredChar getChar(Position pos) {
        return getCell(pos).getChar();
    }

    public void removeGameCharacter(GameCharacter mob) {
        MapCell cell = getCell(mob.getPosition());
        mob.setPositionOnMap(null);
        cell.setGameCharacter(null);
    }

    public void putGameCharacter(GameCharacter mob, Position pos) {
        getCell(pos).setGameCharacter(mob);
        mob.setPositionOnMap(new PositionOnMap(pos, this));
    }

    public void putItem(GameItem item, Position pos) {
        getCell(pos).putItem(item);
    }

    public boolean isCellPassable(Position pos) {
        return getCell(pos).canBePassed;
    }

    public boolean isCellTransparent(Position pos) {
        return getCell(pos).transparent;
    }

    public void moveGameCharacter(GameCharacter mob, Position pos) {
        removeGameCharacter(mob);
        putGameCharacter(mob, pos);
    }

    public boolean isSomeoneHere(Position pos) {
        return getCell(pos).gameCharacter != null;
    }

    public GameCharacter getGameCharacter(Position pos) {
        return getCell(pos).gameCharacter;
    }

    /**
     * Method to fetch free cell randomly positioned on map.
     * Current implementation relies on the fact that the map is sparse and can run into infinite loop otherwise.
     *
     * @return passable map Position on which none stands
     */
    public Position getRandomFreePosition() {
        Position pos = getRandomPositionInsideMap();
        while (isSomeoneHere(pos) || !isCellPassable(pos))
            pos = getRandomPositionInsideMap();
        return pos;
    }

    private MapCell[][] getPartOfMap(int lX, int uX, int lY, int uY) {
        // l stands for lower bound and u stands for upper
        MapCell[][] result = new MapCell[uX - lX][uY - lY];

        for (int x = lX; x < uX; x++)
            for (int y = lY; y < uY; y++) {
                if (x < mapWidth && x >= 0 && y < mapHeight && y >= 0)
                    result[x - lX][y - lY] = map[x][y];
                else
                    result[x - lX][y - lY] = null;
            }

        return result;
    }

    private MapCell[][] getPartOfMap(Position pos, int width, int height) {
        return getPartOfMap(
                pos.getX() - (int) Math.floor(width / 2.0),
                pos.getX() + (int) Math.ceil(width / 2.0),
                pos.getY() - (int) Math.floor(height / 2.0),
                pos.getY() + (int) Math.ceil(height / 2.0)
        );
    }

    public boolean[][] getTransparentCells(Position pos, int width, int height) {

        MapCell[][] partOfMap = getPartOfMap(pos, width, height);

        boolean[][] array = new boolean[partOfMap.length][partOfMap[0].length];

        for (int y = 0, i = 0; y < partOfMap[0].length; y++, i++)
            for (int x = 0, j = 0; x < partOfMap.length; x++, j++)
                if (partOfMap[x][y] != null)
                    array[j][i] = partOfMap[x][y].transparent;

        return array;
    }

    public ColoredChar[][] getVisibleChars(Position pos, int width, int height) {

        MapCell[][] partOfMap = getPartOfMap(pos, width, height);

        ColoredChar[][] result = new ColoredChar[partOfMap.length][partOfMap[0].length];

        for (int y = 0, i = 0; y < partOfMap[0].length; y++, i++)
            for (int x = 0, j = 0; x < partOfMap.length; x++, j++) {
                if (partOfMap[x][y] != null)
                    result[j][i] = partOfMap[x][y].visibleChar;
                else
                    result[j][i] = ColoredChar.NIHIL;
            }

        return result;
    }

    public int getWidth() {
        return mapWidth;
    }

    public int getHeight() {
        return mapHeight;
    }

    public int getWidthOnScreen() {
        return mapWidthOnScreen;
    }

    public int getHeightOnScreen() {
        return mapHeightOnScreen;
    }

    public boolean isInsideMap(Position pos) {
        return pos.getX() < mapWidth && pos.getX() >= 0 && pos.getY() < mapHeight
                && pos.getY() >= 0;
    }

    public boolean isInsideMapScreen(Position pos) {
        return pos.getX() < mapWidthOnScreen && pos.getX() >= 0
                && pos.getY() < mapHeightOnScreen && pos.getY() >= 0;
    }

    private Position getRandomPositionInsideMap() {
        Random rand = new Random();
        return Position.getPosition(rand.nextInt(mapWidth - 2) + 1, rand.nextInt(mapHeight - 2) + 1);
    }

}
