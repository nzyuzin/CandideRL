package game.items;

import game.utility.ColoredChar;

public class MiscItem extends AbstractItem {
	
	public MiscItem(String description, ColoredChar onMap, int weight, int size) {
		super("corpse", description, onMap, weight, size, 1);
	}
	
}