package game;

public class GameObject {
	
	protected String name;
	protected String description;
	
	public GameObject(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
