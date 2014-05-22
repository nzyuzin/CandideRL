/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game.map.cells;

import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.items.GameItem;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.interfaces.Visible;

import java.util.List;

public interface MapCell extends GameObject, Visible {

    ColoredChar getDefaultChar();

    void chooseCharOnMap();

    boolean isTransparent();

    boolean isPassable();

    MapCell setGameCharacter(GameCharacter mob);

    GameCharacter getGameCharacter();

    void putItem(GameItem item);

    void removeItem(GameItem item);

    List<GameItem> getListOfItems();

}
