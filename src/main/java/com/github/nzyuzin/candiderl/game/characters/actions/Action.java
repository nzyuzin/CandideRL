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

import com.google.common.base.Optional;

public interface Action {
    /**
     * Executes the action
     * @return list of events that should be processed after the execution
     */
    ActionResult execute();

    /**
     * Checks if the action can be executed
     * @return Optional.of(errorMessage) if the action cannot be executed. Optional.absent() otherwise
     */
    Optional<String> failureReason();
}
