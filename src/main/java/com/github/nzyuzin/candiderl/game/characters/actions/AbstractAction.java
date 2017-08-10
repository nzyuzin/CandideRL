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

package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.google.common.base.Optional;

abstract class AbstractAction implements Action {
    private final GameCharacter performer;
    private boolean executed;

    public AbstractAction(GameCharacter subject) {
        this.performer = subject;
    }

    protected GameCharacter getPerformer() {
        return performer;
    }

    protected abstract ActionResult doExecute();

    public final ActionResult execute() {
        if (!executed) {
            try {
                return doExecute();
            } finally {
                executed = true;
            }
        } else {
            throw new ActionAlreadyExecutedException();
        }
    }

    protected Optional<String> none() {
        return Optional.absent();
    }

    protected Optional<String> failure(final String reason) {
        return Optional.of(reason);
    }

    protected Optional<String> failure() {
        return Optional.of("");
    }

}
