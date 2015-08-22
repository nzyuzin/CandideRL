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

package com.github.nzyuzin.candiderl.game.characters;

import com.github.nzyuzin.candiderl.game.AbstractGameObject;
import com.github.nzyuzin.candiderl.game.characters.actions.GameAction;
import com.github.nzyuzin.candiderl.game.characters.actions.HitGameAction;
import com.github.nzyuzin.candiderl.game.characters.actions.MovementGameAction;
import com.github.nzyuzin.candiderl.game.items.GameItem;
import com.github.nzyuzin.candiderl.game.items.MiscItem;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Direction;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;

import java.util.ArrayDeque;
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

    protected double attackRate;
    protected Attributes attributes;
    protected boolean canTakeDamage;

    AbstractGameCharacter(String name, String description, int HP) {
        super(name, description);
        maxHP = HP;
        currentHP = HP;
        this.canTakeDamage = true;
        this.attackRate = 1.0;
        attributes = new Attributes();
        gameActions = new ArrayDeque<>();
    }

    @Override
    public boolean hasAction() {
        return !gameActions.isEmpty();
    }

    @Override
    public void addAction(GameAction action) {
        gameActions.add(action);
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
    public PositionOnMap getPositionOnMap() {
        return position;
    }

    @Override
    public void setPositionOnMap(PositionOnMap position) {
        this.position = position;
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
    public boolean canPerformAction() {
        return hasAction() && gameActions.peek().canBeExecuted();
    }

    @Override
    public void performAction() {
        // TODO make use of action points
        if (gameActions.isEmpty()) return;
        gameActions.poll().execute();
    }

    @Override
    public void hit(Position pos) {
        addAction(new HitGameAction(this, pos));
    }

    @Override
    public int roleDamageDice() {
        Random dice = new Random();
        return (int) ((dice.nextInt(20) + this.attributes.strength) * attackRate);
    }

    @Override
    public void move(Direction there) {
        if (there != null)
            addAction(new MovementGameAction(this, there));
    }

    @Override
    public void takeDamage(int damage) {
    /* TODO
	 * if takes 0 as arguments - attacker missed,
	 * otherwise it should apply armor coefficient to damage and then subtract it from current hp.
	 */
        currentHP -= damage;
    }

    @Override
    public GameItem getCorpse() {
        return new MiscItem("Corpse of " + this.getName(), "A corpse",
                ColoredChar.getColoredChar(this.charOnMap.getChar(),
                        this.charOnMap.getForeground(), ColoredChar.RED), 50, 50);
    }

    @Override
    public ColoredChar getChar() {
        return this.charOnMap;
    }

}
