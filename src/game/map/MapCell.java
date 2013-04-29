package game.map;

import game.characters.GameCharacter;
import game.utility.VisibleCharacters;
import game.utility.interfaces.GameItem;
import game.utility.interfaces.Visible;
import game.utility.ColoredChar;
import game.GameObject;

import java.util.ArrayList;

abstract class MapCell extends GameObject implements Visible {
	protected final ColoredChar charOnMap;
	protected ColoredChar visibleChar;
	protected boolean transparent = false;
	
	protected boolean canBePassed = false;
	protected int passageCost = 0;
	
	protected GameCharacter gameCharacter = null;
	protected ArrayList<GameItem> gameItems = null;
	
	protected MapCell(String name, String desc, ColoredChar onMap) {
		super(name, desc);
		canBePassed = false;
		charOnMap = onMap;
		visibleChar = onMap;
		gameItems = new ArrayList<GameItem>();
		transparent = false;
	}
	
	public ColoredChar getChar() {
		return visibleChar;
	}
	
	protected void chooseCharOnMap() {
		if ( gameCharacter != null )
			visibleChar = gameCharacter.getChar();
		else
			if ( !gameItems.isEmpty() )
				visibleChar = gameItems.get(0).getChar();
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
		super("Wall", "A regular rock wall.", new ColoredChar(VisibleCharacters.WALL, ColoredChar.YELLOW));
		canBePassed = false;
		transparent = false;
	}
}

class Floor extends MapCell {	
	Floor() {
		super("Floor", "Rough rock floor.", new ColoredChar(VisibleCharacters.FLOOR));
		canBePassed = true;
		passageCost = 100;
		transparent = true;
	}
}