package creatures;

public class NPC extends Creature {
	
	protected static int ID = 0; 
	protected final int id;
	
	public NPC(String name) {
		super();
		id = ID++;
		this.name = name;
		this.charOnMap = 'g';
		maxHP = 100;
		currentHP = 100;
		attributes.strength = 8;
		attributes.dexterity = 8;
		attributes.intellegence = 8;
		attributes.armor = 0;
		attackRate = 1.0;
		canTakeDamage = true;
	}
}
