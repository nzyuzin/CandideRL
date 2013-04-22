package game.characters;

public final class Player extends GameCharacter {
	
	public Player(String name) {
		super(name, "It's you.", 100);
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