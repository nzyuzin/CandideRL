package characters;

import utility.Position;

public final class Player extends GameCharacter {
	
	public static int[][] distanceToPlayer;
	
	public Player(String name, Position pos) {
		super(pos);
		this.name = name;
		this.charOnMap = utility.VisibleCharacters.PLAYER;
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
}
