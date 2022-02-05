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
import com.github.nzyuzin.candiderl.game.characters.actions.Action;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.google.common.base.Optional;

public final class Player extends AbstractGameCharacter {

    public Player(GameState gameState, final String name) {
        super(gameState, name, "Yet another wanderer in forgotten land", Races.HUMAN.get());
        this.charOnMap = ColoredChar .getColoredChar('@');
    }

    public void skipTurn() {
        setAction(getActionFactory().newSkipTurnAction(this));
    }

    @Override
    public void setAction(Action action) {
        final Optional<String> errorMessage = action.failureReason();
        if (!errorMessage.isPresent()) {
            super.setAction(action);
        } else {
            addMessage(errorMessage.get());
        }
    }
}
