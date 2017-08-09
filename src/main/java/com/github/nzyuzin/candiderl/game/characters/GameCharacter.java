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

import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.characters.actions.GameAction;
import com.github.nzyuzin.candiderl.game.characters.interfaces.Damageable;
import com.github.nzyuzin.candiderl.game.characters.interfaces.Movable;
import com.github.nzyuzin.candiderl.game.characters.interfaces.Visible;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

public interface GameCharacter extends GameObject, HasAttributes, Movable, Damageable, Visible {

    @Override
    MutableAttributes getAttributes();

    boolean hasAction();
    void addAction(GameAction action);
    void removeCurrentAction();

    boolean canPerformAction();
    List<Event> performAction();

    boolean hasMessages();
    void addMessage(String message);
    String removeMessage();

    boolean isDead();
    Position getPosition();

    Map getMap();
    PositionOnMap getPositionOnMap();
    MapCell getMapCell();
    void setPositionOnMap(PositionOnMap position);

    int getCurrentHp();

    ImmutableList<Item> getItems();
    void addItem(Item item);
    void removeItem(Item item);

    void pickupItem(Item item);
    void dropItem(Item item);

    ImmutableList<BodyPart> getBodyParts();

    default ImmutableList<BodyPart> getBodyParts(BodyPart.Type type) {
        return ImmutableList.copyOf(Iterables.filter(getBodyParts(), input -> type.equals(input.getType())));
    }

    Optional<Item> getItem(BodyPart slot);
    void setItem(BodyPart slot, Item item);

    void hit(PositionOnMap pos);
    void move(PositionOnMap pos);
    void openDoor(PositionOnMap pos);
    void closeDoor(PositionOnMap pos);

    int rollDamageDice();

    Item die();
}
