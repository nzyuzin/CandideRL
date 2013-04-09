package creatures;

public final class Hero extends Creature {
	public Hero(String name) {
		super();
		this.name = name;
		this.charOnMap = '@';
		maxHP = 100;
		currentHP = 100;
		attributes.strength = 8;
		attributes.dexterity = 8;
		attributes.intellegence = 8;
		attributes.armor = 0;
		attackRate = 10.0;
		canTakeDamage = true;
	}
}
