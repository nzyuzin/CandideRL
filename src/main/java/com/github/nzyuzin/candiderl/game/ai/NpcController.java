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

package com.github.nzyuzin.candiderl.game.ai;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.actions.HitGameAction;
import com.github.nzyuzin.candiderl.game.characters.actions.MovementGameAction;
import com.github.nzyuzin.candiderl.game.utility.Direction;

public class NpcController {

    private final GameCharacter target;
    private final int opeationalRange;
    private PathFinder path;

    public NpcController(GameCharacter target, int operationalRange) {
        this.opeationalRange = operationalRange;
        this.target = target;
    }

    public void chooseAction(GameCharacter mob) {
        if (target.isDead()) return;
        if (target.getPosition().distanceTo(mob.getPosition()) < 2) {
            mob.hit(target.getPosition());
        } else {
            moveToTarget(mob);
        }
    }

    public void chooseActionInDirection(GameCharacter mob, Direction there) {
        MovementGameAction move = new MovementGameAction(mob, there);
        if (move.canBeExecuted()) {
            mob.addAction(move);
            return;
        }
        HitGameAction hit = new HitGameAction(mob, Direction.applyDirection(mob.getPosition(), there));
        if (hit.canBeExecuted()) {
            mob.addAction(hit);
        }
    }

    private void moveToTarget(GameCharacter mob) {
        // TODO: implementation of path-finding and corresponding AI actions has to be re-thought and rewritten
        if (mob.getPosition().distanceTo(target.getPosition()) > opeationalRange)
            return;

        if (target.getPositionOnMap().getLastPos() != target.getPosition())
            path = new PathFinder(target.getPosition(), opeationalRange, target.getPositionOnMap().getMap());
        mob.move(path.chooseQuickestWay(mob.getPosition()));
    }

}
