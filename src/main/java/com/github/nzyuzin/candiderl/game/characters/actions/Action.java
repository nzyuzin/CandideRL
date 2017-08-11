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

import javax.annotation.Nonnull;

public interface Action extends Comparable<Action> {

    /**
     * Returns the character that performs the action
     * @return character performing the action
     */
    @Nonnull GameCharacter getPerformer();

    /**
     * Checks if the action can be executed
     * @return Optional.of(errorMessage) if the action cannot be executed. Optional.absent() otherwise
     */
    @Nonnull Optional<String> failureReason();

    /**
     * Executes the action
     * @return list of events that should be processed after the execution
     */
    @Nonnull ActionResult execute();

    /**
     * Returns the time at which the action was created
     * @return time in turn units at which the action was created
     */
    int getCreationTime();

    /**
     * Return the time at which the action is predicted to be executed
     * @return time in turn units at which the action is predicted to be executed
     */
    int getExecutionTime();

    /**
     * Returns the time needed to execute the action
     * @return an amount of turn units that takes to execute the action
     */
    int getDelay();

    /**
     * Returns action priority compared to other actions. 100 is the default value
     * @return action priority
     */
    int getPriority();
}
