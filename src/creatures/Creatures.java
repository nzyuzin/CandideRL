package creatures;
import movement.*;
import map.Containable;
import map.Map;

final class Attributes {
	short strength;
	short dexterity;
	short intellegence;
	short armor;
}

public abstract class Creatures extends Containable implements Movable {
	static int id;
	short currenthp;
	short maxhp;
	String name;
	Attributes attributes;
	char icon;
	
	public boolean canMoveThere(Direction there) {
		return Map.canMoveContent(this, there);
	}
	
	public void moveThere(Direction there) {
		if(canMoveThere(there))
			Map.moveContent(this, there);
		else if((Map.getContent(this, there)).type == "enemy")
			//TODO hit(there);
			return;
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
		currenthp -= damage;
	}
}