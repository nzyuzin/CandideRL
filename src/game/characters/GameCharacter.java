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

import game.characters.actions.*;
import game.utility.Direction;
import game.utility.Position;
import game.utility.ColoredChar;
import game.utility.interfaces.*;
import game.items.MiscItem;
import game.GameObject;

import java.util.Random;
import java.util.ArrayDeque;
import java.util.Queue;

// GameCharacter shouldn't know about existence of Map neither of GameUI nor of GameEngine.
// Every actions that is performed on Map is supposed to be expressed through GameAction classes
// and added to queue, which is field of this class.

public abstract class GameCharacter extends GameObject implements Movable, Damageable, Visible  {
	
	protected final class Attributes {
		public short strength;
		public short dexterity;
		public short intellegence;
		public short armor;
		
		public Attributes() {
			this.strength = 8;
			this.armor = 0;
			this.dexterity = 8;
			this.intellegence = 8;
		}
	}
	
	protected Queue<GameAction> gameActions = null;
	
	protected Position position= null;
	protected Position lastPosition = null;
	protected ColoredChar charOnMap = null;
	
	protected int currentHP;
	protected int maxHP;
	protected double speed = 1;
	protected String name;
	
	protected double attackRate;
	protected Attributes attributes;
	protected boolean canTakeDamage;
	protected int actionPointsOnCon;    //
	protected int currentActionPoints;  // Those three fields are of no use for now.
	protected int maximumActionPoints;  //

	
	GameCharacter(String name, String description, int HP) {
		super(name, description);
		maxHP = HP;
		currentHP = HP;
		this.canTakeDamage = true;
		this.attackRate = 1.0;
		attributes = new Attributes();
		gameActions = new ArrayDeque<GameAction>();
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
		return position;
	}
	
	public int getCurrentHP() {
		return currentHP;
	}
	
	public int getMaxHP() {
		return maxHP;
	}
	
	public void setPosition(Position position) {
		this.lastPosition = this.position;
		this.position = position;
	}
	
	public Position getLastPosition() {
		return this.lastPosition;
	}
	
	public boolean canPerformAction() {
		if ( hasAction() )
			return gameActions.peek().canBeExecuted();
		return false;
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
	
	public MiscItem getCorpse() {
		return new MiscItem("Corpse of " + this.getName(), 
				new ColoredChar(this.charOnMap.getChar(), 
						this.charOnMap.getColor().getForeground(), ColoredChar.RED), 50, 50);
	}
	
	public ColoredChar getChar() {
		return this.charOnMap;
	}
	
}