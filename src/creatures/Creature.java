package creatures;

import movement.*;
import map.Map;
import map.exceptions.*;
import java.util.Random;

public abstract class Creature implements Movable {
	
	protected final class Attributes {
		short strength;
		short dexterity;
		short intellegence;
		short armor;
	}
	
	static int id;
	
	public int posX = -1;
	public int posY = -1;
	public char charOnMap = '?';
	
	short currentHP;
	short maxHP;
	float attackRate;
	String name;
	Attributes attributes;
	
	
	public boolean canMove(Direction there) {
		return Map.canMoveCreature(this, there);
	}
	
	public void move(Direction there) {
		try {
			Map.moveCreature(this, there);
				
		} catch (CellIsTaken e) {
			if (Map.canHit(this, there))
				hit(there);
		}
	}
	
	void hit(Direction there) {
	/*  TODO
	 *  Hit should generate integer which is calculated,
	 *  depending on attackers attributes and random number.
	 *  This integer is passed to takeAHit method later on,
	 *  separately from this method.
	 */
		Random rand = new Random();
		int damage = rand.nextInt((int) (attributes.strength * attackRate));
		Map.getCreature(this, there).takeAHit(damage);
	}
	
	void takeAHit(int damage) {
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