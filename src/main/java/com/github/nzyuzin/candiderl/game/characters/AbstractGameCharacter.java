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
import com.github.nzyuzin.candiderl.game.characters.actions.GameAction;
import com.github.nzyuzin.candiderl.game.characters.actions.HitInMeleeAction;
import com.github.nzyuzin.candiderl.game.characters.actions.MoveToNextCellAction;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.items.MiscItem;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

abstract class AbstractGameCharacter extends AbstractGameObject implements GameCharacter {

    protected static final int DEFAULT_HP = 100;

    protected static final class Attributes {
        public short strength;
        public short dexterity;
        public short intelligence;
        public short armor;

        public Attributes() {
            this.strength = 8;
            this.armor = 0;
            this.dexterity = 8;
            this.intelligence = 8;
        }
    }

    protected Queue<GameAction> gameActions = null;

    protected PositionOnMap position;
    protected ColoredChar charOnMap = null;

    protected int currentHP;
    protected int maxHP;

    protected List<Item> items;
    protected List<ItemSlot> itemSlots;

    protected double attackRate;
    protected Attributes attributes;
    protected boolean canTakeDamage;

    AbstractGameCharacter(String name, String description, int HP, List<ItemSlot> itemSlots) {
        super(name, description);
        maxHP = HP;
        currentHP = HP;
        this.canTakeDamage = true;
        this.attackRate = 1.0;
        this.items = Lists.newArrayListWithCapacity(52);
        this.itemSlots = itemSlots;
        attributes = new Attributes();
        gameActions = new ArrayDeque<>();
    }

    @Override
    public boolean hasAction() {
        return !gameActions.isEmpty();
    }

    @Override
    public void addAction(GameAction action) {
        if (action.canBeExecuted()) {
            gameActions.add(action);
        }
    }

    @Override
    public void removeCurrentAction() {
        gameActions.poll();
    }

    @Override
    public boolean isDead() {
        return currentHP <= 0;
    }

    @Override
    public Position getPosition() {
        return position.getPosition();
    }

    @Override
    public Map getMap() {
        return position.getMap();
    }

    @Override
    public PositionOnMap getPositionOnMap() {
        return position;
    }

    @Override
    public void setPositionOnMap(PositionOnMap position) {
        this.position = position;
    }

    @Override
    public MapCell getMapCell() {
        return getPositionOnMap().getMap().getCell(getPosition());
    }

    @Override
    public int getCurrentHP() {
        return currentHP;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public short getStrength() {
        return attributes.strength;
    }

    @Override
    public short getDexterity() {
        return attributes.dexterity;
    }

    @Override
    public short getIntelligence() {
        return attributes.intelligence;
    }

    @Override
    public ImmutableList<Item> getItems() {
        return ImmutableList.copyOf(items);
    }

    @Override
    public void addItem(final Item item) {
        items.add(item);
    }

    @Override
    public void removeItem(final Item item) {
        items.remove(item);
        for (final ItemSlot slot : itemSlots) {
            if (slot.getItem().isPresent() && slot.getItem().get() == item) {
                slot.removeItem();
            }
        }
    }

    @Override
    public void pickupItem(final Item item) {
        final MapCell currentCell = getMapCell();
        Preconditions.checkArgument(currentCell.getItems().contains(item), "No such item on the current cell!");
        currentCell.removeItem(item);
        this.addItem(item);
    }

    @Override
    public void dropItem(final Item item) {
        Preconditions.checkArgument(getItems().contains(item), "No such item in the inventory!");
        getMapCell().putItem(item);
        this.removeItem(item);
    }

    @Override
    public ImmutableList<ItemSlot> getItemSlots() {
        return ImmutableList.copyOf(itemSlots);
    }

    @Override
    public Optional<Item> getItem(final ItemSlot slot) {
        return itemSlots.get(itemSlots.indexOf(slot)).getItem();
    }

    @Override
    public void setItem(final ItemSlot slot, final Item item) {
        itemSlots.get(itemSlots.indexOf(slot)).setItem(item);
    }

    @Override
    public short getArmor() {
        return attributes.armor;
    }

    @Override
    public boolean canPerformAction() {
        return !isDead() && hasAction() && gameActions.peek().canBeExecuted();
    }

    @Override
    public List<Event> performAction() {
        // TODO make use of action points
        if (gameActions.isEmpty()) {
            return Collections.emptyList();
        }
        return gameActions.poll().execute();
    }

    @Override
    public void hit(Position pos) {
        GameCharacter target = getMap().getGameCharacter(pos);
        if (target != null) {
            addAction(new HitInMeleeAction(this, target));
        }
    }

    @Override
    public void move(Position pos) {
        addAction(new MoveToNextCellAction(this, this.getMap(), pos));
    }

    @Override
    public int rollDamageDice() {
        Random dice = new Random();
        return (int) (dice.nextInt(this.attributes.strength) * attackRate);
    }

    @Override
    public void takeDamage(int damage) {
        if (isDead()) {
            return;
        }
        /* TODO
         * if takes 0 as arguments - attacker missed,
         * otherwise it should apply armor coefficient to damage and then subtract it from current hp.
         */
        currentHP -= damage;
        if (currentHP < 0) {
            getMap().putItem(this.die(), this.getPosition());
            getMap().removeGameCharacter(this);
        }
    }

    @Override
    public Item die() {
        return new MiscItem("Corpse of " + this.getName(), "A corpse",
                ColoredChar.getColoredChar(this.charOnMap.getChar(),
                        this.charOnMap.getForeground(), ColoredChar.RED), 50, 50);
    }

    @Override
    public ColoredChar getChar() {
        return this.charOnMap;
    }

}
