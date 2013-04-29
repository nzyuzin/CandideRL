package game.characters;

import game.utility.ColoredChar;

public class NPC extends GameCharacter {
	
	protected static int ID = 0; 
	protected final int id;
	
	public NPC(String name, String description, ColoredChar onMap) {
		super(name, description, 100);
		id = ID++;
		this.charOnMap = onMap;
	}

}
