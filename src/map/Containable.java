package map;

public abstract class Containable {
	public char charOnMap;
	int x;
	int y;
	public String type;
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