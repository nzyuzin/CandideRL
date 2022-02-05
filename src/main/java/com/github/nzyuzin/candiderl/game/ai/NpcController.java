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

import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.actions.MoveToNextCellAction;
import com.github.nzyuzin.candiderl.game.utility.Position;

public class NpcController {

    private final GameCharacter target;
    private final int operationalRange;
    private final GameState gameInformation;

    public NpcController(GameCharacter target, int operationalRange, GameState gameState) {
        this.target = target;
        this.operationalRange = operationalRange;
        this.gameInformation = gameState;
    }

    public void act(GameCharacter mob) {
        if (target.isDead()) return;
        if (target.getPosition().distanceTo(mob.getPosition()) < 2) {
            if (target.getAction().isPresent() && target.getAction().get() instanceof MoveToNextCellAction) {
                final MoveToNextCellAction moveAction = (MoveToNextCellAction) target.getAction().get();
                if (mob.getPosition().distanceTo(moveAction.getPosition()) >= 2
                        && moveAction.getExecutionTime() <= gameInformation.getCurrentTime() + mob.getAttackDelay()) {
                    mob.move(target.getPosition());
                    return;
                }
            }
            mob.hit(target.getPosition());
        } else {
            moveToTarget(mob);
        }
    }

    private void moveToTarget(GameCharacter mob) {
        if (mob.getPosition().distanceTo(target.getPosition()) > operationalRange)
            return;
        final PathFinder path =
                new PathFinder(target.getPosition(), operationalRange, target.getMap());
        final Position newPosition = path.findNextMove(mob.getPosition());
        if (!newPosition.equals(mob.getPosition())) {
            mob.move(newPosition);
        }
    }

}
