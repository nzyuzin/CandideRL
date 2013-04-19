package game.items;

public class MiscItem extends AbstractItem {
	
	public MiscItem(String description, char onMap, int weight, int size) {
		super("corpse", description,onMap, weight, size, 1);
	}
	
}