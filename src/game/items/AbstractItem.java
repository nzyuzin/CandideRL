package game.items;

import game.utility.interfaces.GameItem;
import game.GameObject;
import game.utility.Color;

public abstract class AbstractItem extends GameObject implements GameItem {
	
	protected char charOnMap = '?';
	protected int quantity;
	protected int weight;
	protected int size;
	protected Color color = null;
	
	AbstractItem(String name, String description, char onMap, Color col, int weight, int size, int quantity) {
		super(name, description);
		this.charOnMap = onMap;
		this.weight = weight;
		this.size = size;
		this.quantity = quantity;
		this.color = col;
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
	
	public char getCharOnMap() {
		return charOnMap;
	}
	
	public void setCharOnMap(char onMap) {
		charOnMap = onMap;
	}
	
	public Color getColor() {
		return this.color;
	}
}