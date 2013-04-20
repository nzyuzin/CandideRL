package game.characters;

import game.utility.Position;

public class NPC extends GameCharacter {
	
	protected static int ID = 0; 
	protected final int id;
	
	public NPC(String name, String description, char onMap, Position pos) {
		super(name, description, 100, pos);
		id = ID++;
		this.setCharOnMap(onMap);
	}

}
