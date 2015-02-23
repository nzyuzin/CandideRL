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

package com.github.nzyuzin.candiderl.game.map.cells;

import com.github.nzyuzin.candiderl.game.AbstractGameObject;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.items.GameItem;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractMapCell extends AbstractGameObject implements MapCell {
    protected final ColoredChar charOnMap;
    protected final boolean transparent;
    protected final boolean canBePassed;

    protected ColoredChar visibleChar;

    protected GameCharacter gameCharacter = null;
    protected List<GameItem> gameItems = null;

    protected AbstractMapCell(String name, String desc, ColoredChar onMap, boolean transp, boolean canBePassed) {
        super(name, desc);
        this.canBePassed = canBePassed;
        this.charOnMap = onMap;
        this.visibleChar = onMap;
        this.gameItems = new ArrayList<>();
        this.transparent = transp;
    }

    @Override
    public ColoredChar getChar() {
        return visibleChar;
    }

    @Override
    public ColoredChar getDefaultChar() {
        return charOnMap;
    }

    @Override
    public void chooseCharOnMap() {
        if (gameCharacter != null)
            visibleChar = gameCharacter.getChar();
        else if (!gameItems.isEmpty())
            visibleChar = gameItems.get(0).getChar();
        else
            visibleChar = charOnMap;
    }

    @Override
    public boolean isTransparent() {
        return this.transparent;
    }

    @Override
    public boolean isPassable() {
        return this.canBePassed;
    }

    @Override
    public MapCell setGameCharacter(GameCharacter mob) {
        this.gameCharacter = mob;
        chooseCharOnMap();
        return this;
    }

    @Override
    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    @Override
    public void putItem(GameItem item) {
        gameItems.add(item);
    }

    @Override
    public void removeItem(GameItem item) {
        gameItems.remove(item);
    }

    @Override
    public List<GameItem> getListOfItems() {
        ArrayList<GameItem> result = new ArrayList<>();
        result.addAll(gameItems);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

