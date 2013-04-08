package map;

import creatures.Creature;

public abstract class MapCell {
	protected char charOnMap;
	protected char visibleChar;
	protected boolean canBePassed = false;
	protected Creature creature = null;
	
	protected void chooseCharOnMap() {
		if (creature != null)
			visibleChar = creature.charOnMap;
		else visibleChar = charOnMap;
	}
}

class Wall extends MapCell {
	Wall() {
		charOnMap = '#';
		visibleChar = '#';
		canBePassed = false;
	}
}

class Floor extends MapCell {	
	Floor() {
		canBePassed = true;
		visibleChar = '.';
		charOnMap = '.';
	}
}