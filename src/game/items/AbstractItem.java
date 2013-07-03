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

package game.items;

import game.utility.interfaces.GameItem;
import game.GameObject;
import game.utility.ColoredChar;

public abstract class AbstractItem extends GameObject implements GameItem {
	
	protected final ColoredChar charOnMap;
	protected final int weight;
	protected final int size;
	
	protected int quantity;
	
	AbstractItem(String name, String description, ColoredChar onMap, 
			int weight, int size, int quantity) {
		super(name, description);
		this.charOnMap = onMap;
		this.weight = weight;
		this.size = size;
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getSize() {
		return size;
	}
	
	public ColoredChar getChar() {
		return charOnMap;
	}
	
}