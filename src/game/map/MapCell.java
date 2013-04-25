package game.map;

import game.characters.GameCharacter;
import game.utility.VisibleCharacters;
import game.utility.interfaces.GameItem;
import game.utility.interfaces.Visible;
import game.GameObject;

import java.util.ArrayList;

abstract class MapCell extends GameObject implements Visible {
	protected final char charOnMap;
	protected char visibleChar;
	protected boolean transparent;
	
	protected boolean canBePassed = false;
	protected int passageCost = 0;
	
	protected GameCharacter gameCharacter = null;
	protected ArrayList<GameItem> gameItems = null;
	
	protected MapCell(String name, String desc, char onMap) {
		super(name, desc);
		canBePassed = false;
		charOnMap = onMap;
		gameItems = new ArrayList<GameItem>();
		transparent = false;
	}
	
	public char getCharOnMap() {
		return visibleChar;
	}
	
	public void setCharOnMap(char onMap) { 
		visibleChar = onMap;
	}
	
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
		super("Wall", "A regular rock wall.", VisibleCharacters.WALL);
		visibleChar = VisibleCharacters.WALL;
		canBePassed = false;
	}
}

class Floor extends MapCell {	
	Floor() {
		super("Floor", "Rough rock floor.", VisibleCharacters.FLOOR);
		canBePassed = true;
		visibleChar = VisibleCharacters.FLOOR;
		passageCost = 100;
		transparent = true;
	}
}