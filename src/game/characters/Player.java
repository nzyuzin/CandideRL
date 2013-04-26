package game.characters;

import game.map.FieldOfView;

public final class Player extends GameCharacter {
	
	FieldOfView los = null;
	
	public Player(String name) {
		super(name, "It's you.", 100);
		this.name = name;
		this.charOnMap = game.utility.VisibleCharacters.PLAYER;
		currentActionPoints = 50;
		speed = 1;
		
		los = new FieldOfView(this, 10);
	}
	
	public String getStats() {
		return "Name:\n" + name +
				"\nHP: " + currentHP + "/" + maxHP + "\n";
	}
	
	public String getVisibleMap() {
		return los.toString();
	}
	
}