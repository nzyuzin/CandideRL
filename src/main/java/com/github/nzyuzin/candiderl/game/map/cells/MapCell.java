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

import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.interfaces.Visible;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.cells.effects.MapCellEffect;

import java.util.List;
import java.util.Optional;

public interface MapCell extends GameObject, Visible {

    boolean isTransparent();

    boolean isPassable();

    boolean isSeenByPlayer();
    void markSeenByPlayer();

    MapCell setGameCharacter(GameCharacter mob);

    Optional<GameCharacter> getGameCharacter();

    void putItem(Item item);

    void removeItem(Item item);

    List<Item> getItems();

    void applyEffects();
    void addEffect(MapCellEffect effect);
}
