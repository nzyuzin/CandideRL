package game.items;

import game.utility.interfaces.GameItem;

public abstract class AbstractItem implements GameItem {
	
	protected char charOnMap = '?';
	protected int quantity;
	protected int weight;
	protected int size;
	
	AbstractItem(char onMap, int weight, int size, int quantity) {
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
	
	public char getCharOnMap() {
		return charOnMap;
	}
	
	public void setCharOnMap(char onMap) {
		charOnMap = onMap;
	}
}
