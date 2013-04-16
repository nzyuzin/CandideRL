package game.map;

import game.characters.GameCharacter;
import game.utility.VisibleCharacters;
import game.utility.interfaces.GameItem;
import game.utility.interfaces.Visible;
import java.util.ArrayList;

abstract class MapCell implements Visible {
	protected final char charOnMap;
	protected char visibleChar;
	
	protected boolean canBePassed = false;
	protected int passageCost = 0;
	
	protected GameCharacter gameCharacter = null;
	protected ArrayList<GameItem> gameItems = null;
	
	protected MapCell(char onMap) {
		charOnMap = onMap;
		gameItems = new ArrayList<GameItem>();
	}
	
	public char getCharOnMap() {
		return charOnMap;
	}
	
	public void setCharOnMap(char onMap) { }
	
	protected void chooseCharOnMap() {
		if ( gameCharacter != null )
			visibleChar = gameCharacter.getCharOnMap();
		else
			if ( !gameItems.isEmpty() )
				visibleChar = gameItems.get(0).getCharOnMap();
			else 
				visibleChar = charOnMap;
	}
	
	protected void setGameCharacter(GameCharacter mob) {
		this.gameCharacter = mob;
		chooseCharOnMap();
	}
	
	protected GameCharacter getGameCharacter() {
		return gameCharacter;
	}
	
	protected void putItem(GameItem item) {
		gameItems.add(item);
	}
	
	protected void removeItem(GameItem item) {
		gameItems.remove(item);
	}
	
	protected GameItem[] getListOfItems() {
		return (GameItem[]) gameItems.toArray();
		
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