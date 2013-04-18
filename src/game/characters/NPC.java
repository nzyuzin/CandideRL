package game.characters;

import game.utility.Position;

public class NPC extends GameCharacter {
	
	protected static int ID = 0; 
	protected final int id;
	
	public NPC(String name, char onMap, Position pos) {
		super(pos);
		id = ID++;
		this.name = name;
		this.setCharOnMap(onMap);
		maxHP = 100;
		currentHP = 100;
		attributes.strength = 8;
		attributes.dexterity = 8;
		attributes.intellegence = 8;
		attributes.armor = 0;
		attackRate = 1.0;
		canTakeDamage = true;
	}
}