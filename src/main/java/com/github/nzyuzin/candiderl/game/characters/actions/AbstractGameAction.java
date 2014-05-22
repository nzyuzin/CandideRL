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

package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;

// GameActions connect GameCharacters and classes from another packages,
// such as Map, so GameCharacters will only operate with highly abstract methods.

abstract class AbstractGameAction implements GameAction {

    protected final GameCharacter performer; // The one who performs an action

    protected int actionPointsLeft = 0;  // Not implemented yet.

    public abstract boolean canBeExecuted();

    /* Perform an action. Action is not supposed to be used after doing this method once. */
    public abstract void execute();

    public AbstractGameAction(GameCharacter subject) {
        this.performer = subject;
    }

    public int actionPointsLeft() {  // Not implemented yet.
        return (this.actionPointsLeft < 0 ? 0 : this.actionPointsLeft);
    }

    public void setActionPointsLeft(int points) {  // Not implemented yet.
        this.actionPointsLeft = points;
    }

}
