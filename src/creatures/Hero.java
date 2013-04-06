package creatures;

public final class Hero extends Creatures {
	public Hero(String name) {
		this.name = name;
		this.charOnMap = '@';
		this.type = "hero";
	}
}
