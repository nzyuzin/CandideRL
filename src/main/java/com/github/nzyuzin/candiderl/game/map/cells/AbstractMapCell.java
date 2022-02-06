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

package com.github.nzyuzin.candiderl.game.map.cells;

import com.github.nzyuzin.candiderl.game.AbstractGameObject;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.cells.effects.MapCellEffect;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractMapCell extends AbstractGameObject implements MapCell {
    protected ColoredChar charOnMap;
    protected boolean transparent;
    protected boolean isPassable;
    protected boolean seenByPlayer;

    protected Optional<GameCharacter> gameCharacter = Optional.empty();
    protected List<Item> items = null;
    protected List<MapCellEffect> effects = null;

    protected AbstractMapCell(String name, String desc, ColoredChar onMap, boolean transp, boolean isPassable) {
        super(name, desc);
        this.charOnMap = onMap;
        this.isPassable = isPassable;
        this.transparent = transp;
        this.seenByPlayer = false;
        this.items = Lists.newArrayList();
        this.effects = Lists.newArrayList();
    }

    @Override
    public ColoredChar getChar() {
        if (gameCharacter.isPresent())
            return gameCharacter.get().getChar();
        else if (!items.isEmpty())
            return items.get(0).getChar();
        else
            return charOnMap;
    }

    @Override
    public boolean isTransparent() {
        return this.transparent;
    }

    @Override
    public boolean isPassable() {
        return this.isPassable;
    }

    @Override
    public boolean isSeenByPlayer() {
        return seenByPlayer;
    }

    @Override
    public void markSeenByPlayer() {
        this.seenByPlayer = true;
    }

    @Override
    public MapCell setGameCharacter(GameCharacter mob) {
        this.gameCharacter = Optional.ofNullable(mob);
        return this;
    }

    @Override
    public Optional<GameCharacter> getGameCharacter() {
        return gameCharacter;
    }

    @Override
    public void putItem(Item item) {
        items.add(item);
    }

    @Override
    public void removeItem(Item item) {
        items.remove(item);
    }

    @Override
    public List<Item> getItems() {
        ArrayList<Item> result = new ArrayList<>();
        result.addAll(items);
        return result;
    }

    @Override
    public void applyEffects() {
        for (MapCellEffect effect : effects) {
            effect.apply(this);
            if (effect.getDuration() <= 0) {
                effects.remove(effect);
            }
        }
    }

    @Override
    public void addEffect(MapCellEffect effect) {
        this.effects.add(effect);
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
