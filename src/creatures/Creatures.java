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
	
//TODO	boolean hit(Direction where)
//TODO	public boolean canMoveHere(Direction where);
//TODO	public boolean moveHere(Direction where);
	
}
