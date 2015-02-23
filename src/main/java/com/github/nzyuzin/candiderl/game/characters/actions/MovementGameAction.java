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
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.*;

public final class MovementGameAction extends AbstractGameAction {

    private final Position position;
    private final Map map;

    public MovementGameAction(GameCharacter subject, Direction there) {
        super(subject);
        position = Direction.applyDirection(subject.getPosition(), there);
        map = subject.getPositionOnMap().getMap();
    }

    public boolean canBeExecuted() {
        return !getPerformer().isDead() && getPerformer().getPosition().distanceTo(position) < 2
                && map.isCellPassable(position) && !map.isSomeoneHere(position);
    }

    public void execute() {
        map.moveGameCharacter(getPerformer(), position);
    }
}
