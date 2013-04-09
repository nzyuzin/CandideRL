package creatures;

import map.Map;
import java.util.Random;
import utility.Direction;
import utility.interfaces.Damagable;
import utility.interfaces.Movable;

public abstract class Creature implements Movable, Damagable  {
	
	protected final class Attributes {
		public short strength;
		public short dexterity;
		public short intellegence;
		public short armor;
	}
	
	protected static int ID = 0; 
	
	protected final int id;
	
	public int posX = -1;
	public int posY = -1;
	public char charOnMap = '?';
	
	public short currentHP;
	public short maxHP;
	protected double attackRate;
	protected String name;
	protected Attributes attributes;
	protected boolean canTakeDamage;
	
	Creature() {
		id = ID++;
		attributes = new Attributes();
	}
	
	public boolean canMove(Direction there) {
		return Map.canMoveCreature(this, there);
	}
	
	public void move(Direction there) {
			Map.moveCreature(this, there);
	}
	
	public boolean canHit(Direction there) {
		return Map.canHit(this, there);
	}
	
	protected void hit(Direction there) {
	/*  TODO
	 *  Hit should generate integer which is calculated,
	 *  depending on attackers attributes and random number.
	 *  This integer is passed to takeAHit method later on,
	 *  separately from this method.
	 */
		Random rand = new Random();
		int damage = rand.nextInt((int) (attributes.strength * attackRate));
		Map.getCreature(this, there).takeDamage(damage);
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