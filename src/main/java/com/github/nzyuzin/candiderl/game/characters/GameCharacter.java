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
import com.github.nzyuzin.candiderl.game.items.GameItem;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

public interface GameCharacter extends GameObject, Movable, Damageable, Visible {
    boolean hasAction();
    void addAction(GameAction action);
    void removeCurrentAction();
    boolean isDead();
    Position getPosition();

    Map getMap();
    PositionOnMap getPositionOnMap();
    void setPositionOnMap(PositionOnMap position);

    int getCurrentHP();
    int getMaxHP();
    short getStrength();
    short getDexterity();
    short getIntelligence();
    short getArmor();

    ImmutableList<ItemSlot> getItemSlots();

    Optional<GameItem> getItem(ItemSlot slot);
    void setItem(ItemSlot slot, GameItem item);

    boolean canPerformAction();
    List<Event> performAction();
    void hit(Position pos);

    void move(Position pos);

    int rollDamageDice();

    GameItem die();
}
