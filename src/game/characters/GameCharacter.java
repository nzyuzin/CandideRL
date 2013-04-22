package game.characters;

import game.characters.actions.*;
import game.utility.Direction;
import game.utility.Position;
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
	protected char charOnMap = '?';       // this charOnMap should never appear on map if everything goes fine. Should be overwritten by successor classes.
	
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
		
//		GameAction current = gameActions.peek();
//		if (current.actionPointsLeft() < 0)
//			gameActions.poll().execute();
//		else {
//			current.setActionPointsLeft(current.actionPointsLeft() - currentActionPoints);
//		}
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
		return new MiscItem("Corpse of " + this.getName(),'*', 50, 50);
	}
	
	public char getCharOnMap() {
		return charOnMap;
	}
	
	public void setCharOnMap(char onMap) {
		charOnMap = onMap;
	}
}