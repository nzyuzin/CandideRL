package game.characters;

import game.characters.actions.*;
import game.utility.Direction;
import game.utility.Position;
import game.utility.interfaces.*;

import java.util.Random;
import java.util.ArrayDeque;
import java.util.Queue;

// GameCharacter shouldn't know about existence of Map neither of GameUI nor of GameEngine.
// Every actions that is performed on Map is supposed to be expressed through GameAction classes
// and added to queue, which is field of this class.

public abstract class GameCharacter implements Movable, Damageable  {
	
	protected final class Attributes {
		public short strength;
		public short dexterity;
		public short intellegence;
		public short armor;
	}
	
	protected Queue<GameAction> gameActions = null;
	
	public Position position= null;
	public char charOnMap = '?';       // this charOnMap should never appear on map if everything goes fine. Should be overwritten by successor classes.
	
	public short currentHP;
	public short maxHP;
	public double speed = 1;
	public String name;
	
	protected double attackRate;
	protected Attributes attributes;
	protected boolean canTakeDamage;
	protected int actionPointsOnCon;    //
	protected int currentActionPoints;  // Those three fields are of no use for now.
	protected int maximumActionPoints;  //

	
	GameCharacter(Position pos) {
		position = pos;
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
	/*  TODO
	 *  Hit should generate integer which is calculated,
	 *  depending on attackers attributes and random number.
	 *  This integer is passed to takeAHit method later on,
	 *  separately from this method.
	 */
		addAction(new HitGameAction(this, pos));
	}
	
	public int roleDamageDice() {
		return 42;
	}
	
	public void move(Direction there) {
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
		Random rand = new Random();
		int chancetoevade = rand.nextInt(50) + attributes.dexterity;
		if (chancetoevade < 50)
			currentHP -= (damage - attributes.armor);
	}
}