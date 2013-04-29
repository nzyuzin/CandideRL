package game.items;

import game.utility.interfaces.GameItem;
import game.GameObject;
import game.utility.ColoredChar;

public abstract class AbstractItem extends GameObject implements GameItem {
	
	protected final ColoredChar charOnMap;
	protected final int weight;
	protected final int size;
	
	protected int quantity;
	
	AbstractItem(String name, String description, ColoredChar onMap, int weight, int size, int quantity) {
		super(name, description);
		this.charOnMap = onMap;
		this.weight = weight;
		this.size = size;
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getSize() {
		return size;
	}
	
	public ColoredChar getChar() {
		return charOnMap;
	}
	
}