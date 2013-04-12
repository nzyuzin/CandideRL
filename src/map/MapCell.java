package map;

import characters.GameCharacter;
import utility.VisibleCharacters;

abstract class MapCell {
	protected final char charOnMap;
	protected char visibleChar;
	protected boolean canBePassed = false;
	protected GameCharacter gameCharacter = null;
	protected int passageCost = 0;
	
	protected MapCell(char onMap) {
		charOnMap = onMap;
	}
	
	protected void chooseCharOnMap() {
		if (gameCharacter != null)
			visibleChar = gameCharacter.charOnMap;
		else visibleChar = charOnMap;
	}
}

class Wall extends MapCell {
	Wall() {
		super(VisibleCharacters.WALL);
		visibleChar = VisibleCharacters.WALL;
		canBePassed = false;
	}
}

class Floor extends MapCell {	
	Floor() {
		super(VisibleCharacters.FLOOR);
		canBePassed = true;
		visibleChar = VisibleCharacters.FLOOR;
		passageCost = 100;
	}
}