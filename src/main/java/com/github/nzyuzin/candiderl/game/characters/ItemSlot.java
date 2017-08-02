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

package com.github.nzyuzin.candiderl.game.characters;

import com.github.nzyuzin.candiderl.game.AbstractGameObject;
import com.github.nzyuzin.candiderl.game.items.GameItem;

import java.util.Optional;

public class ItemSlot extends AbstractGameObject {

    enum Type {
        HAND, BODY, HEAD, LEGS, RING, AMULET;
    }

    private Optional<GameItem> gameItem = Optional.empty();
    private final Type type;

    public ItemSlot(String name, Type type) {
        super(name, name);
        this.type = type;
    }

    public Optional<GameItem> getItem() {
        return gameItem;
    }

    public void setItem(final GameItem gameItem) {
        this.gameItem = Optional.of(gameItem);
    }

}
