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
import com.github.nzyuzin.candiderl.game.characters.actions.Action;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionFactory;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionResult;
import com.github.nzyuzin.candiderl.game.characters.actions.OpenCloseDoorAction;
import com.github.nzyuzin.candiderl.game.characters.bodyparts.BodyPart;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.items.MiscItem;
import com.github.nzyuzin.candiderl.game.items.Weapon;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

abstract class AbstractGameCharacter extends AbstractGameObject implements GameCharacter {

    private final ActionFactory actionFactory;

    private final Queue<String> gameMessages;

    protected PositionOnMap position;
    protected ColoredChar charOnMap;

    protected int currentHp;

    protected Optional<Action> action;

    protected List<Item> items;
    protected List<BodyPart> bodyParts;

    protected MutableAttributes attributes;
    protected boolean canTakeDamage;

    AbstractGameCharacter(String name, String description, Race race, ActionFactory actionFactory) {
        super(name, description);
        this.currentHp = race.getMaxHp();
        this.attributes = new MutableAttributes(race.getAttributes());
        this.bodyParts = race.getBodyParts();
        this.canTakeDamage = true;
        this.items = Lists.newArrayListWithCapacity(52);
        this.action = Optional.absent();
        this.gameMessages = new ArrayDeque<>();
        this.actionFactory = actionFactory;
    }

    protected ActionFactory getActionFactory() {
        return actionFactory;
    }

    @Override
    public MutableAttributes getAttributes() {
        return attributes;
    }

    @Override
    public ImmutableList<String> pollMessages() {
        final ImmutableList<String> result = ImmutableList.copyOf(gameMessages);
        gameMessages.clear();
        return result;
    }

    protected void addMessage(final String message) {
        gameMessages.add(message);
    }

    @Override
    public boolean hasAction() {
        return action.isPresent();
    }

    @Override
    public Optional<Action> getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        if (!this.isDead()) {
            this.action = Optional.of(action);
        }
    }

    @Override
    public ActionResult performAction() {
        if (!action.isPresent()) {
            return ActionResult.EMPTY;
        }
        final Action action = this.action.get();
        this.action = Optional.absent();
        if (action.failureReason().isPresent()) {
            return new ActionResult(action.failureReason().get());
        } else {
            return action.execute();
        }
    }

    @Override
    public int getActionDelay() {
        return action.isPresent() ? action.get().getDelay() : 0;
    }

    @Override
    public boolean isDead() {
        return currentHp <= 0;
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
        return getPositionOnMap().getMapCell();
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
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
        for (final BodyPart slot : bodyParts) {
            if (slot.getItem().isPresent() && slot.getItem().get() == item) {
                slot.removeItem();
            }
        }
    }

    @Override
    public void pickupItem(final Item item) {
        setAction(actionFactory.newPickupItemAction(this, item));
    }

    @Override
    public void dropItem(final Item item) {
        setAction(actionFactory.newDropItemAction(this, item));
    }

    @Override
    public void wieldItem(Item item) {
        setAction(actionFactory.newWieldItemAction(this, item));
    }

    @Override
    public ImmutableList<BodyPart> getBodyParts() {
        return ImmutableList.copyOf(bodyParts);
    }

    @Override
    public Optional<Item> getItem(final BodyPart slot) {
        return bodyParts.get(bodyParts.indexOf(slot)).getItem();
    }

    @Override
    public void setItem(final BodyPart slot, final Item item) {
        bodyParts.get(bodyParts.indexOf(slot)).setItem(item);
    }

    @Override
    public void hit(final PositionOnMap pos) {
        setAction(actionFactory.newHitAction(this, pos.getGameCharacter().get(), getAttackDelay()));
    }

    @Override
    public int getAttackDelay() {
        return 100;
    }

    @Override
    public void move(final PositionOnMap pos) {
        setAction(actionFactory.newMoveAction(this, pos, getMovementDelay()));
    }

    @Override
    public int getMovementDelay() {
        return 100;
    }

    @Override
    public void traverseStairs(Stairs.Type type) {
        setAction(actionFactory.newTraverseStairsAction(this, type));
    }

    @Override
    public void openDoor(PositionOnMap pos) {
        this.setAction(actionFactory.newOpenCloseDoorAction(this, pos, OpenCloseDoorAction.Type.OPEN));
    }

    @Override
    public void closeDoor(PositionOnMap pos) {
        this.setAction(actionFactory.newOpenCloseDoorAction(this, pos, OpenCloseDoorAction.Type.CLOSE));
    }

    @Override
    public int rollDamageDice() {
        final Random dice = new Random();
        int baseDamage = dice.nextInt(this.getStrength());
        for (final Weapon weapon : getWeapons()) {
            baseDamage += dice.nextInt(weapon.getDamage());
        }
        return baseDamage;
    }

    private Set<Weapon> getWeapons() {
        final Set<Weapon> weapons = Sets.newHashSet();
        for (final BodyPart bodyPart : getBodyParts(BodyPart.Type.HAND)) {
            if (bodyPart.getItem().isPresent()) {
                if (bodyPart.getItem().get() instanceof Weapon) {
                    weapons.add((Weapon) bodyPart.getItem().get());
                }
            }
        }
        return weapons;
    }

    @Override
    public void takeDamage(int damage) {
        /* TODO
         * if takes 0 as arguments - attacker missed,
         * otherwise it should apply armor coefficient to damage and then subtract it from current hp.
         */
        currentHp -= damage;
        if (isDead()) {
            getMap().putItem(this.die(), this.getPosition());
            getMap().removeGameCharacter(this);
        }
    }

    @Override
    public Item die() {
        return new MiscItem("Corpse of " + this.getName(), "A corpse",
                ColoredChar.getColoredChar('?', this.charOnMap.getForeground(), ColoredChar.RED), 50, 50);
    }

    @Override
    public ColoredChar getChar() {
        return this.charOnMap;
    }

}
