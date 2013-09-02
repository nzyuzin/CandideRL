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

import game.GameObject;
import game.characters.GameCharacter;
import game.utility.ColoredChar;
import game.utility.Position;
import game.utility.VisibleCharacters;
import game.utility.interfaces.GameItem;
import game.utility.interfaces.Visible;

import java.util.ArrayList;

abstract class MapCell extends GameObject implements Visible {
	protected final ColoredChar charOnMap;
	protected final boolean transparent;
	protected final Position position;
	protected final boolean canBePassed;

    protected ColoredChar visibleChar;

	protected GameCharacter gameCharacter = null;
	protected ArrayList<GameItem> gameItems = null;

	protected MapCell(String name, String desc, ColoredChar onMap, boolean transp, boolean canBePassed, Position pos) {
		super(name, desc);
		this.canBePassed = canBePassed;
		this.charOnMap = onMap;
		this.visibleChar = onMap;
		this.gameItems = new ArrayList<GameItem>();
		this.transparent = transp;
        this.position = pos;
	}

	@Override
	public ColoredChar getChar() {
		return visibleChar;
	}

	protected void chooseCharOnMap() {
		if (gameCharacter != null)
			visibleChar = gameCharacter.getChar();
		else if (!gameItems.isEmpty())
			visibleChar = gameItems.get(0).getChar();
		else
			visibleChar = charOnMap;
	}

	protected MapCell setGameCharacter(GameCharacter mob) {
		this.gameCharacter = mob;
		chooseCharOnMap();
		return this;
	}

	protected GameCharacter getGameCharacter() {
		return gameCharacter;
	}

	protected Position getPosition() {
		return this.position;
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MapCell))
            return false;
        return this.name.equals(((MapCell) object).getName())
                && this.position.equals(((MapCell) object).getPosition())
                && this.charOnMap.equals(((MapCell) object).charOnMap)
                && this.transparent == ((MapCell) object).transparent
                && this.canBePassed == ((MapCell) object).canBePassed
                && this.position.equals(((MapCell) object).getPosition());
    }

}

class Wall extends MapCell {
	private Wall(Position pos) {
		super("Wall", "A regular rock wall.", new ColoredChar(VisibleCharacters.WALL, ColoredChar.YELLOW),
                false, false, pos);
	}

    static Wall getWall(Position position) {
        MapCell cellOnMap = position.getMap().getCell(position);
        if (cellOnMap instanceof Wall)
            return (Wall) cellOnMap;
        return new Wall(position);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Wall))
            return false;
        return super.equals(object);
    }
}

class Floor extends MapCell {
	private Floor(Position pos) {
		super("Floor", "Rough rock floor.", new ColoredChar(
				VisibleCharacters.FLOOR), true, true, pos);
	}

    static Floor getFloor(Position position) {
        MapCell cellOnMap = position.getMap().getCell(position);
        if (cellOnMap instanceof Floor)
            return (Floor) cellOnMap;
        return new Floor(position);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Floor))
            return false;
        return super.equals(object);
    }
}