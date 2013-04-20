package game.characters;

import game.utility.Position;

public final class Player extends GameCharacter {
	
	public Player(String name, Position pos) {
		super(name, "It's you.", 100, pos);
		this.name = name;
		this.charOnMap = game.utility.VisibleCharacters.PLAYER;
		currentActionPoints = 50;
		speed = 1;
	}
	
	public String getStats() {
		return "Name:\n" + name +
				"\nHP: " + currentHP + "/" + maxHP + "\n";
	}
	
}