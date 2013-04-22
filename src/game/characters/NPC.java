package game.characters;

public class NPC extends GameCharacter {
	
	protected static int ID = 0; 
	protected final int id;
	
	public NPC(String name, String description, char onMap) {
		super(name, description, 100);
		id = ID++;
		this.setCharOnMap(onMap);
	}

}
