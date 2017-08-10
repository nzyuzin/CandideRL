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

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.characters.actions.SkipTurnAction;
import com.github.nzyuzin.candiderl.game.fov.FieldOfVision;
import com.github.nzyuzin.candiderl.game.fov.FovFactory;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

public final class Player extends AbstractGameCharacter {

    private FieldOfVision fov = null;

    public Player(final String name) {
        super(name, "Yet another wanderer in forgotten land", Races.HUMAN.get());
        this.charOnMap = ColoredChar .getColoredChar('@');
        fov = FovFactory.getInstance().getFOV(this, GameConfig.VIEW_DISTANCE_LIMIT);
    }

    public ColoredChar[][] getVisibleMap(int width, int height) {
        return fov.getVisibleCells(width, height);
    }

    public void skipTurn() {
        addAction(new SkipTurnAction(this));
    }

}
