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

import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

abstract class AbstractAction implements Action {
    private final GameCharacter performer;
    private final int delay;
    private final int creationTime;
    private final int priority;
    private boolean executed;

    public AbstractAction(@Nonnull GameCharacter performer, int currentTime, int delay) {
        this(performer, currentTime, delay, 100);
    }

    public AbstractAction(@Nonnull GameCharacter performer, int currentTime, int delay, int priority) {
        Preconditions.checkNotNull(performer);
        Preconditions.checkArgument(delay >= 0);
        this.performer = performer;
        this.creationTime = currentTime;
        this.delay = delay;
        this.priority = priority;
    }

    @Nonnull
    public GameCharacter getPerformer() {
        return performer;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getCreationTime() {
        return creationTime;
    }

    @Override
    public int getExecutionTime() {
        return creationTime + delay;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Nonnull
    protected abstract ActionResult doExecute();

    @Nonnull
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

    protected String describeAction(final GameCharacter performer, final String verb, final GameObject object) {
        return describeAction(performer, verb, object, "");
    }

    protected String describeAction(GameCharacter performer, String verb, GameObject object, String optionalInfo) {
        final String objectString = object instanceof Player ? "you" : object.toString();
        return describeAction(performer, verb, objectString, optionalInfo);
    }

    protected String describeAction(GameCharacter performer, String verb, String objectString) {
        return describeAction(performer, verb, objectString, "");
    }

    protected String describeAction(GameCharacter performer, String verb, String objectString, String optionalInfo) {
        final String performerString = performer instanceof Player ? "You" : performer.toString();
        final String verbString;
        if (performer instanceof Player) {
            verbString = verb;
        } else {
            if (verb.contains(" ")) {
                final int spaceIndex = verb.indexOf(' ');
                verbString = verb.substring(0, spaceIndex) + "s" + verb.substring(spaceIndex);
            } else {
                verbString = verb + "s";
            }
        }
        return performerString + " " + verbString + " " + objectString
                + (!optionalInfo.isEmpty() ? " (" +  optionalInfo + ")" : "");
    }

    @Override
    public int compareTo(Action o) {
        final int timeComparison = this.getExecutionTime() - o.getExecutionTime();
        if (timeComparison == 0) { // player acts the first if the time is the same
            if (this.getPerformer() instanceof Player) {
                return -1;
            } else if (o.getPerformer() instanceof Player) {
                return 1;
            } else {
                return this.getPriority() - o.getPriority();
            }
        }
        return timeComparison;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
