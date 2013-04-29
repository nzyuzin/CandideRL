package game.items;

import game.utility.Color;

public class MiscItem extends AbstractItem {
	
	public MiscItem(String description, char onMap, Color col, int weight, int size) {
		super("corpse", description, onMap, col, weight, size, 1);
	}
	
}