package game.characters;

import game.utility.Color;

public class NPC extends GameCharacter {
	
	protected static int ID = 0; 
	protected final int id;
	
	public NPC(String name, String description, char onMap, Color col) {
		super(name, description, 100);
		id = ID++;
		this.setCharOnMap(onMap);
		this.color = col;
	}

}
