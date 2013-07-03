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

package game.characters;

import game.map.FieldOfView;
import game.utility.ColoredChar;

public final class Player extends GameCharacter {
	
	private FieldOfView fov = null;
	private static Player player = new Player();
	
	private Player() {
		super("Player", "It's you.", 100);
		this.charOnMap = new ColoredChar(game.utility.VisibleCharacters.PLAYER);
		currentActionPoints = 50;
		speed = 1;
		
		fov = new FieldOfView(this, 9);
	}
	
	public String getStats() {
		return "Name:\n" + name +
				"\nHP: " + currentHP + "/" + maxHP + "\n";
	}
	
	public ColoredChar[][] getVisibleMap() {
		return fov.toColoredCharArray();
	}
	
	public static Player getInstance() {
		return player;
	}
	
}