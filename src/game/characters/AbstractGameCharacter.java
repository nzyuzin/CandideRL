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

package game.characters;

import game.AbstractGameObject;
import game.characters.actions.GameAction;
import game.characters.actions.HitGameAction;
import game.characters.actions.MovementGameAction;
import game.items.GameItem;
import game.items.MiscItem;
import game.utility.ColoredChar;
import game.utility.Direction;
import game.utility.Position;
import game.utility.PositionOnMap;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

abstract class AbstractGameCharacter extends AbstractGameObject implements GameCharacter {

    protected final class Attributes {
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
    protected double speed = 1;

    protected double attackRate;
    protected Attributes attributes;
    protected boolean canTakeDamage;
    protected int actionPointsOnCon;    //
    protected int currentActionPoints;  // Those three fields are of no use for now.
    protected int maximumActionPoints;  //


    AbstractGameCharacter(String name, String description, int HP) {
        super(name, description);
        maxHP = HP;
        currentHP = HP;
        this.canTakeDamage = true;
        this.attackRate = 1.0;
        attributes = new Attributes();
        gameActions = new ArrayDeque<>();
    }


    public boolean hasAction() {
        return !gameActions.isEmpty();
    }

    public void addAction(GameAction action) {
        gameActions.add(action);
    }

    public void removeCurrentAction() {
        gameActions.poll();
    }

    public boolean isDead() {
        return currentHP <= 0;
    }

    public Position getPosition() {
        return position.getPosition();
    }

    public PositionOnMap getPositionOnMap() {
        return position;
    }

    public void setPositionOnMap(PositionOnMap position) {
        this.position = position;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public boolean canPerformAction() {
        return hasAction() && gameActions.peek().canBeExecuted();
    }

    public void performAction() {
        // TODO make use of action points
        if (gameActions.isEmpty()) return;
        gameActions.poll().execute();
    }

    public void breakActionQueue() {
        gameActions = new ArrayDeque<GameAction>();
    }

    public void hit(Position pos) {
        addAction(new HitGameAction(this, pos));
    }

    public int roleDamageDice() {
        Random dice = new Random();
        return (int) ((dice.nextInt(20) + this.attributes.strength) * attackRate);
    }

    public void move(Direction there) {
        if (there != null)
            addAction(new MovementGameAction(this, there));
    }

    public boolean canTakeDamage() {
        return canTakeDamage;
    }

    public void takeDamage(int damage) {
    /* TODO
	 * if takes 0 as arguments - attacker missed,
	 * otherwise it should apply armor coefficient to damage and then subtract it from currenthp.
	 */
        currentHP -= damage;
    }

    public GameItem getCorpse() {
        return new MiscItem("Corpse of " + this.getName(),
                ColoredChar.getColoredChar(this.charOnMap.getChar(),
                        this.charOnMap.getForeground(), ColoredChar.RED), 50, 50);
    }

    public ColoredChar getChar() {
        return this.charOnMap;
    }

}
