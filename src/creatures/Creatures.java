package creatures;
import movement.*;
import map.Containable;

public abstract class Creatures extends Containable implements Movable {
	int id;
	short currenthp;
	short maxhp;
	String name;
	Attributes attributes;
	char icon;
	
//TODO	int hit(Direction there)  -- Hit should generate integer which is calculated depending on attackers attributes and random number. This integer is passed to takeAHit method later on, separately from this method.
//TODO	public boolean canMoveHere(Direction there);
//TODO	public boolean moveHere(Direction there);
//TODO  void takeAHit(int damage)  -- if takes 0 as arguments attacker missed, otherwise it should apply armor coefficient to damage and then subtract it from currenthp.
}
