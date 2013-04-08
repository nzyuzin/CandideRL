package creatures;

import movement.*;
import map.Map;
import map.exceptions.*;

final class Attributes {
	short strength;
	short dexterity;
	short intellegence;
	short armor;
}

public abstract class Creature implements Movable {
	static int id;
	
	public int posX = -1;
	public int posY = -1;
	public char charOnMap = '?';
	
	short currentHP;
	short maxHP;
	String name;
	Attributes attributes;
	
	
	public boolean canMove(Direction there) {
		return Map.canMoveCreature(this, there);
	}
	
	public void move(Direction there) {
		try {
			Map.moveCreature(this, there);
				
		} catch (CellIsTaken e) {
			hit(there);
		}
	}
	
	int hit(Direction there) {
	/*  TODO
	 *  Hit should generate integer which is calculated,
	 *  depending on attackers attributes and random number.
	 *  This integer is passed to takeAHit method later on,
	 *  separately from this method.
	 */
		return 17;
	}
	void takeAHit(int damage) {
	/* TODO
	 * if takes 0 as arguments attacker missed,
	 * otherwise it should apply armor coefficient to damage and then subtract it from currenthp.	
	 */
		currentHP -= damage;
	}
}