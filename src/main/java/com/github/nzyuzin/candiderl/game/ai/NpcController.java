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

package com.github.nzyuzin.candiderl.game.ai;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;

public class NpcController {

    private final GameCharacter target;
    private final int operationalRange;

    public NpcController(GameCharacter target, int operationalRange) {
        this.operationalRange = operationalRange;
        this.target = target;
    }

    public void chooseAction(GameCharacter mob) {
        if (target.isDead()) return;
        if (target.getPosition().distanceTo(mob.getPosition()) < 2) {
            mob.hit(target.getPositionOnMap());
        } else {
            moveToTarget(mob);
        }
    }

    private void moveToTarget(GameCharacter mob) {
        // TODO: implementation of path-finding and corresponding AI actions has to be re-thought and rewritten
        if (mob.getPosition().distanceTo(target.getPosition()) > operationalRange)
            return;

        final PathFinder path =
                new PathFinder(target.getPosition(), operationalRange, target.getPositionOnMap().getMap());
        mob.move(mob.getPositionOnMap().newPosition(path.findNextMove(mob.getPosition())));
    }

}
