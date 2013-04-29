package game.characters;

import game.map.FieldOfView;
import game.utility.Color;

public final class Player extends GameCharacter {
	
	FieldOfView los = null;
	
	public Player(String name) {
		super(name, "It's you.", 100);
		this.name = name;
		this.charOnMap = game.utility.VisibleCharacters.PLAYER;
		currentActionPoints = 50;
		speed = 1;
		this.color = new Color();
		
		los = new FieldOfView(this, 9);
	}
	
	public String getStats() {
		return "Name:\n" + name +
				"\nHP: " + currentHP + "/" + maxHP + "\n";
	}
	
	public char[][] getVisibleMap() {
		return los.toCharArray();
	}
	
}