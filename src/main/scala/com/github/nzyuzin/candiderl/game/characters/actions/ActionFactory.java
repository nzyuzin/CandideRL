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

import com.github.nzyuzin.candiderl.game.GameInformation;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Preconditions;

public class ActionFactory {

    private GameInformation gameInformation;
    private MapFactory mapFactory;

    public void setGameInformation(GameInformation gameInformation) {
        Preconditions.checkArgument(this.gameInformation == null, "Game information is already set!");
        this.gameInformation = gameInformation;
    }

    public void setMapFactory(MapFactory mapFactory) {
        Preconditions.checkArgument(this.mapFactory == null, "Map factory is already set!");
        this.mapFactory = mapFactory;
    }

    public MoveToNextCellAction newMoveAction(GameCharacter subject, PositionOnMap position, int delay) {
        return new MoveToNextCellAction(subject, position, gameInformation.getCurrentTime(), delay);
    }

    public CastExplosionAction newCastExplosionAction(GameCharacter subject, PositionOnMap position) {
        return new CastExplosionAction(subject, position, gameInformation.getCurrentTime());
    }

    public DropItemAction newDropItemAction(GameCharacter subject, Item item) {
        return new DropItemAction(subject, item, gameInformation.getCurrentTime());
    }

    public HitInMeleeAction newHitAction(GameCharacter performer, GameCharacter target, int delay) {
        return new HitInMeleeAction(performer, target, gameInformation.getCurrentTime(), delay);
    }

    public OpenCloseDoorAction newOpenCloseDoorAction(GameCharacter subject, PositionOnMap doorPosition, OpenCloseDoorAction.Type type) {
        return new OpenCloseDoorAction(subject, doorPosition, type, gameInformation.getCurrentTime());
    }

    public PickupItemAction newPickupItemAction(GameCharacter subject, Item item) {
        return new PickupItemAction(subject, item, gameInformation.getCurrentTime());
    }

    public SkipTurnAction newSkipTurnAction(GameCharacter subject) {
        return new SkipTurnAction(subject, gameInformation.getCurrentTime());
    }

    public TraverseStairsAction newTraverseStairsAction(GameCharacter subject, Stairs.Type type) {
        return new TraverseStairsAction(subject, type, mapFactory, gameInformation.getCurrentTime());
    }

    public WieldItemAction newWieldItemAction(GameCharacter subject, Item item) {
        return new WieldItemAction(subject, item, gameInformation.getCurrentTime());
    }
}
