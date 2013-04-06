package map;

public abstract class Containable {
	public char charOnMap;
	int x;
	int y;
}

class Wall extends Containable {
	Wall(char c) {
		charOnMap = c;
	}
}

class Floor extends Containable {	
	Floor(char c) {
		charOnMap = c;
	}
}