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
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

public final class MoveToNextCellAction extends AbstractGameAction {

    private final Position position;
    private final Map map;

    public MoveToNextCellAction(GameCharacter subject, Map map, Position position) {
        super(subject);
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(subject);
        this.map = map;
        this.position = position;
    }

    public boolean canBeExecuted() {
        return !getPerformer().isDead() && map.equals(getPerformer().getMap())
                && getPerformer().getPosition().isAdjacentTo(position)
                && map.isCellPassable(position) && !map.isSomeoneHere(position);
    }

    protected List<Event> doExecute() {
        map.moveGameCharacter(getPerformer(), position);
        return Collections.emptyList(); // TODO: change position event
    }
}
