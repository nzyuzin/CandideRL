/*
 *  This file is part of CandideRL.
 *
 *  CandideRL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CandideRL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

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
		super("Wall", "A regular rock wall.", 
				new ColoredChar(VisibleCharacters.WALL, ColoredChar.YELLOW));
		canBePassed = false;
		transparent = false;
	}
}

class Floor extends MapCell {	
	Floor() {
		super("Floor", "Rough rock floor.", 
				new ColoredChar(VisibleCharacters.FLOOR));
		canBePassed = true;
		passageCost = 100;
		transparent = true;
	}
}