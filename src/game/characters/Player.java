package game.characters;

import game.utility.Position;

public final class Player extends GameCharacter {
	
	public Player(String name, Position pos) {
		super(pos);
		this.name = name;
		this.charOnMap = game.utility.VisibleCharacters.PLAYER;
		maxHP = 100;
		currentHP = 100;
		attributes.strength = 8;
		attributes.dexterity = 8;
		attributes.intellegence = 8;
		attributes.armor = 0;
		attackRate = 10.0;
		canTakeDamage = true;
		currentActionPoints = 50;
		speed = 1;
	}
	
	public String getStats() {
		return "Name:\n" + name +
				"\nHP: " + currentHP + "/" + maxHP + "\n";
	}
	
}