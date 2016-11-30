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

package com.github.nzyuzin.candiderl.game.ui;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

public class VisibleInformation {
    private final ColoredChar[][] visibleMap;
    private final GameCharacter player;
    private final long currentTurn;

    public VisibleInformation(ColoredChar[][] visibleMap, GameCharacter player, long currentTurn) {
        this.visibleMap = visibleMap;
        this.player = player;
        this.currentTurn = currentTurn;
    }

    public ColoredChar[][] getVisibleMap() {
        return visibleMap;
    }

    public GameCharacter getPlayer() {
        return player;
    }

    public long getCurrentTurn() {
        return currentTurn;
    }
}
