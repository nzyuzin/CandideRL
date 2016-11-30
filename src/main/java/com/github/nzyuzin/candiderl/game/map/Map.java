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

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.items.GameItem;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.map.cells.effects.MapCellEffect;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.function.Consumer;

public class Map {
    private final int mapWidth;
    private final int mapHeight;
    private final MapCell[][] map;

    Map(int width, int height) {
        Preconditions.checkArgument(width > 0 && height > 0);
        map = new MapCell[width][height];
        mapWidth = width;
        mapHeight = height;
    }

    MapCell getCell(Position pos) {
        Preconditions.checkArgument(pos.getX() < mapWidth && pos.getX() >= 0 && pos.getY() < mapHeight
                && pos.getY() >= 0);
        return map[pos.getX()][pos.getY()];
    }

    void setCell(Position pos, MapCell cell) {
        setCell(pos.getX(), pos.getY(), cell);
    }

    void setCell(int x, int y, MapCell cell) {
        Preconditions.checkArgument(x < mapWidth && x >= 0 && y < mapHeight && y >= 0);
        map[x][y] = cell;
    }

    public void removeGameCharacter(GameCharacter mob) {
        Preconditions.checkState(mob.getMap().equals(this));
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
        return getCell(pos).isPassable();
    }

    public void moveGameCharacter(GameCharacter mob, Position pos) {
        removeGameCharacter(mob);
        putGameCharacter(mob, pos);
    }

    public boolean isSomeoneHere(Position pos) {
        return getCell(pos).getGameCharacter() != null;
    }

    public GameCharacter getGameCharacter(Position pos) {
        return getCell(pos).getGameCharacter();
    }

    /**
     * Method to fetch free cell randomly positioned on map.
     * Current implementation relies on the fact that the map is sparse and can run into infinite loop otherwise.
     *
     * @return passable map Position on which none stands
     */
    public Position getRandomFreePosition() {
        Position pos = getRandomPositionInsideMap();
        while (isSomeoneHere(pos) || !isCellPassable(pos)) {
            pos = getRandomPositionInsideMap();
        }
        return pos;
    }

    private MapCell[][] getPartOfMap(int lX, int uX, int lY, int uY) {
        // l stands for lower bound and u stands for upper
        MapCell[][] result = new MapCell[uX - lX][uY - lY];
        for (int x = lX; x < uX; x++) {
            for (int y = lY; y < uY; y++) {
                if (x < mapWidth && x >= 0 && y < mapHeight && y >= 0) {
                    result[x - lX][y - lY] = map[x][y];
                } else {
                    result[x - lX][y - lY] = null;
                }
            }
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
        for (int y = 0, i = 0; y < partOfMap[0].length; y++, i++) {
            for (int x = 0, j = 0; x < partOfMap.length; x++, j++) {
                if (partOfMap[x][y] != null) {
                    array[j][i] = partOfMap[x][y].isTransparent();
                }
            }
        }
        return array;
    }

    public ColoredChar[][] getVisibleChars(Position pos, int width, int height) {
        MapCell[][] partOfMap = getPartOfMap(pos, width, height);
        ColoredChar[][] result = new ColoredChar[partOfMap.length][partOfMap[0].length];
        for (int y = 0, i = 0; y < partOfMap[0].length; y++, i++) {
            for (int x = 0, j = 0; x < partOfMap.length; x++, j++) {
                if (partOfMap[x][y] != null)
                    result[j][i] = partOfMap[x][y].getChar();
                else
                    result[j][i] = ColoredChar.NIHIL;
            }
        }
        return result;
    }

    public void addEffectsToArea(Position pos, int width, int height, MapCellEffect effect) {
        map((MapCell cell) -> cell.addEffect(effect), getPartOfMap(pos, width, height));
    }

    public void applyEffects() {
        map(MapCell::applyEffects, map);
    }

    public int getWidth() {
        return mapWidth;
    }

    public int getHeight() {
        return mapHeight;
    }

    private Position getRandomPositionInsideMap() {
        Random rand = new Random();
        return Position.getInstance(rand.nextInt(mapWidth - 2) + 1, rand.nextInt(mapHeight - 2) + 1);
    }

    private void map(final Consumer<MapCell> mapping, final MapCell[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                mapping.accept(map[i][j]);
            }
        }
    }

}
