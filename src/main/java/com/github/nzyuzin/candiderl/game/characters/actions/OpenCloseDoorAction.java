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
import com.github.nzyuzin.candiderl.game.events.AbstractEventContext;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.map.cells.Door;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

public class OpenCloseDoorAction extends AbstractGameAction {

    public enum Type {
        OPEN, CLOSE;
    }

    private final PositionOnMap doorPosition;
    private final Door door;
    private final Type type;

    public OpenCloseDoorAction(GameCharacter subject, PositionOnMap doorPosition, Type type) {
        super(subject);
        Preconditions.checkArgument(doorPosition.getMapCell() instanceof Door, "There's no door on the given position!");
        this.doorPosition = doorPosition;
        this.door = (Door) doorPosition.getMapCell();
        this.type = type;
    }

    @Override
    public Optional<String> failureReason() {
        if (doorPosition.getMap() != getPerformer().getMap()
                || doorPosition.getPosition().distanceTo(getPerformer().getPosition()) >= 2) {
            return failure("No door there!");
        }
        if (type == Type.OPEN) {
            if (door.isClosed()) {
                return none();
            }
        } else {
            if (door.isOpen() && door.getGameCharacter() == null && door.getItems().isEmpty()) {
                return none();
            }
        }
        return failure();
    }

    @Override
    protected List<Event> doExecute() {
        if (type == Type.OPEN) {
            return Collections.singletonList(new Event<AbstractEventContext>() {
                @Override
                public void occur() {
                    door.open();
                }

                @Override
                public String getTextualDescription() {
                    return "You open the door";
                }
            });
        } else {
            return Collections.singletonList(new Event<AbstractEventContext>() {
                @Override
                public void occur() {
                    door.close();
                }

                @Override
                public String getTextualDescription() {
                    return "You close the door";
                }
            });
        }
    }
}
