package map;

public abstract class Containable {
	public char charOnMap;
	protected int x;
	protected int y;
	public String type = "nihil";
}

class Wall extends Containable {
	Wall(char c) {
		charOnMap = c;
		type = "wall";
	}
}

class Floor extends Containable {	
	Floor(char c) {
		charOnMap = c;
		type = "floor";
	}
}