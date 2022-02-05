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

import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public class TraverseStairsAction extends AbstractAction {

    private final Stairs.Type type;
    private final GameState gameState;

    public TraverseStairsAction(GameCharacter subject, Stairs.Type type, GameState gameState, int currentTime) {
        super(subject, currentTime, 100);
        this.type = type;
        this.gameState = gameState;
    }

    @Nonnull
    @Override
    public Optional<String> failureReason() {
        final MapCell currentCell = getPerformer().getMapCell();
        final String directionStr = type == Stairs.Type.UP ? "upwards" : "downwards";
        if (currentCell instanceof Stairs && type == ((Stairs) currentCell).getType()) {
            final Stairs stairs = (Stairs) getPerformer().getMapCell();
            if (type == Stairs.Type.UP && !stairs.getMatchingPosition().isPresent()) {
                return failure("The stairs lead to the surface, it's too early to go there for you");
            } else {
                return none();
            }
        } else {
            return failure("You can't go " + directionStr + " here!");
        }
    }

    @Nonnull
    @Override
    protected ActionResult doExecute() {
        final Stairs stairs = (Stairs) getPerformer().getMapCell();
        if (type == Stairs.Type.UP) {
            moveToNewMap(stairs.getMatchingMap().get(), stairs.getMatchingPosition().get());
            return new ActionResult(describeAction(getPerformer(), "walk up", "the stairs"));
        } else {
            if (stairs.getMatchingPosition().isPresent()) {
                moveToNewMap(stairs.getMatchingMap().get(), stairs.getMatchingPosition().get());
            } else {
                final Map newMap = gameState.getGameFactories().getMapFactory().build(gameState);
                gameState.addMap(newMap);
                final Stairs newUpwardsStairs = (Stairs) newMap.getCell(newMap.getUpwardsStairs());
                newUpwardsStairs.setMatchingStairs(getPerformer().getMap(), getPerformer().getPosition());
                stairs.setMatchingStairs(newMap, newMap.getUpwardsStairs());
                moveToNewMap(newMap, newMap.getUpwardsStairs());
            }
            return new ActionResult(describeAction(getPerformer(), "walk down", "the stairs"));
        }
    }

    private void moveToNewMap(final Map newMap, final Position newPosition) {
        final Map currentMap = getPerformer().getMap();
        currentMap.removeGameCharacter(getPerformer());
        newMap.putGameCharacter(getPerformer(), newPosition);
    }
}
