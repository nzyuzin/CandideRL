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

package com.github.nzyuzin.candiderl.game.characters;

import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionFactory;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

public class Npc extends AbstractGameCharacter {

    public Npc(GameState gameState, String name, String description, Race race, ColoredChar onMap) {
        super(gameState, name, description, race);
        this.charOnMap = onMap;
    }

}
