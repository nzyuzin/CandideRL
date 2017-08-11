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
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public class PickupItemAction extends AbstractAction {

    private final Item item;

    public PickupItemAction(GameCharacter subject, Item item, int currentTime) {
        super(subject, currentTime, 100);
        this.item = item;
    }

    @Nonnull
    @Override
    public Optional<String> failureReason() {
        if (!getPerformer().getMapCell().getItems().contains(item)) {
            return failure("There is no such item here!");
        } else {
            return none();
        }
    }

    @Override
    protected ActionResult doExecute() {
        final MapCell cell = getPerformer().getMapCell();
        cell.removeItem(item);
        getPerformer().addItem(item);
        return new ActionResult(describeAction(getPerformer(), "pick up", item));
    }
}
