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
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public class TraverseStairsAction extends AbstractAction {

    private final Stairs.Type type;
    private final MapFactory mapFactory;

    public TraverseStairsAction(GameCharacter subject, Stairs.Type type, MapFactory mapFactory) {
        super(subject);
        this.type = type;
        this.mapFactory = mapFactory;
    }

    @Nonnull
    @Override
    public Optional<String> failureReason() {
        final PositionOnMap currentPosition = getPerformer().getPositionOnMap();
        final String directionStr = type == Stairs.Type.UP ? "upwards" : "downwards";
        if (currentPosition.getMapCell() instanceof Stairs && type == ((Stairs) currentPosition.getMapCell()).getType()) {
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
            moveToNewMap(stairs.getMatchingPosition().get());
            return new ActionResult(describeAction(getPerformer(), "walk up", "the stairs"));
        } else {
            if (stairs.getMatchingPosition().isPresent()) {
                moveToNewMap(stairs.getMatchingPosition().get());
            } else {
                final Map newMap = mapFactory.build();
                final Stairs newUpwardsStairs = (Stairs) newMap.getUpwardsStairs().getMapCell();
                newUpwardsStairs.setMatchingStairs(getPerformer().getPositionOnMap());
                stairs.setMatchingStairs(newMap.getUpwardsStairs());
                moveToNewMap(newMap.getUpwardsStairs());
            }
            return new ActionResult(describeAction(getPerformer(), "walk down", "the stairs"));
        }
    }

    private void moveToNewMap(final PositionOnMap newPosition) {
        final Map currentMap = getPerformer().getPositionOnMap().getMap();
        currentMap.moveGameCharacter(getPerformer(), newPosition);
    }
}
