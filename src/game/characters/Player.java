package game.characters;

import game.map.FieldOfView;
import game.utility.ColoredChar;

public final class Player extends GameCharacter {
	
	private FieldOfView fov = null;
	
	public Player(String name) {
		super(name, "It's you.", 100);
		this.name = name;
		this.charOnMap = new ColoredChar(game.utility.VisibleCharacters.PLAYER);
		currentActionPoints = 50;
		speed = 1;
		
		fov = new FieldOfView(this, 9);
	}
	
	public String getStats() {
		return "Name:\n" + name +
				"\nHP: " + currentHP + "/" + maxHP + "\n";
	}
	
	public ColoredChar[][] getVisibleMap() {
		return fov.toColoredCharArray();
	}
	
}