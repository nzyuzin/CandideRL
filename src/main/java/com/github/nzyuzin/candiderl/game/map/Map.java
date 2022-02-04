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

import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.cells.Floor;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.map.cells.effects.MapCellEffect;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Map implements GameObject {

    private final String name;

    private final int mapWidth;
    private final int mapHeight;
    private final MapCell[][] map;

    private PositionOnMap upwardsStairs;
    private PositionOnMap downwardsStairs;

    private final List<GameCharacter> characters;

    public Map(String name, int width, int height) {
        Preconditions.checkArgument(width > 0 && height > 0);
        this.name = name;
        this.map = new MapCell[width][height];
        this.mapWidth = width;
        this.mapHeight = height;
        this.characters = Lists.newArrayList();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.name;
    }

    public PositionOnMap getUpwardsStairs() {
        Preconditions.checkNotNull(upwardsStairs, "No upwards stairs on the map!");
        return upwardsStairs;
    }

    public PositionOnMap getDownwardsStairs() {
        Preconditions.checkNotNull(downwardsStairs, "No downwards stairs on the map!");
        return downwardsStairs;
    }

    public ImmutableList<GameCharacter> getCharacters() {
        return ImmutableList.copyOf(characters);
    }

    public MapCell getCell(Position pos) {
        return getCell(pos.getX(), pos.getY());
    }

    public MapCell getCell(int x, int y) {
        Preconditions.checkArgument(x < mapWidth && x >= 0 && y < mapHeight && y >= 0,
                "Illegal position: (" + x + ", " + y + ")");
        return map[x][y];
    }

    public void setCell(Position pos, MapCell cell) {
        setCell(pos.getX(), pos.getY(), cell);
    }

    public void setCell(int x, int y, MapCell cell) {
        Preconditions.checkArgument(x < mapWidth && x >= 0 && y < mapHeight && y >= 0,
                "Illegal position: (" + x + ", " + y + ")");
        if (cell instanceof Stairs) {
            final Stairs stairs = (Stairs) cell;
            if (stairs.getType() == Stairs.Type.DOWN) {
                downwardsStairs = new PositionOnMap(Position.getInstance(x, y), this);
            } else {
                upwardsStairs = new PositionOnMap(Position.getInstance(x, y), this);
            }
        }
        map[x][y] = cell;
    }

    public void removeGameCharacter(GameCharacter mob) {
        Preconditions.checkState(mob.getMap().equals(this), "Given character is not on this map!");
        MapCell cell = getCell(mob.getPosition());
        cell.setGameCharacter(null);
        characters.remove(mob);
    }

    public void putGameCharacter(GameCharacter mob, Position pos) {
        Preconditions.checkArgument(!getCell(pos).getGameCharacter().isPresent(), "The map cell already contains a character!");
        getCell(pos).setGameCharacter(mob);
        mob.setPositionOnMap(new PositionOnMap(pos, this));
        characters.add(mob);
    }

    public void putItem(Item item, Position pos) {
        getCell(pos).putItem(item);
    }

    public void moveGameCharacter(GameCharacter mob, PositionOnMap pos) {
        removeGameCharacter(mob);
        pos.getMap().putGameCharacter(mob, pos.getPosition());
    }

    /**
     * Method to fetch free cell randomly positioned on map.
     * Current implementation relies on the fact that the map is sparse and can run into infinite loop otherwise.
     *
     * @return passable map Position on which none stands
     */
    public Position getRandomFreePosition() {
        Position pos = getRandomPositionInsideMap();
        while (getCell(pos).getGameCharacter().isPresent() || !getCell(pos).isPassable() || !(getCell(pos) instanceof Floor)) {
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

    public boolean isInside(Position pos) {
        return pos.getX() >= 0 && pos.getX() < mapWidth && pos.getY() >= 0 && pos.getY() < mapHeight;
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
