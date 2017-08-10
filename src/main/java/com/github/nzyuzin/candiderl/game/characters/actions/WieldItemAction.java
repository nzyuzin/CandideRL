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
import com.github.nzyuzin.candiderl.game.characters.bodyparts.BodyPart;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.nzyuzin.candiderl.game.characters.bodyparts.BodyPart.Type.HAND;

public class WieldItemAction extends AbstractAction {

    private final Item item;

    public WieldItemAction(GameCharacter subject, Item item) {
        super(subject);
        this.item = item;
    }

    @Override
    public Optional<String> failureReason() {
        if (!getPerformer().getItems().contains(item)) {
            return failure("Item is not in the inventory");
        }
        if (item.getBodyPartTypes().stream().filter(type -> type != HAND).count() > 0) {
            return failure(item + " cannot be wielded only in hands");
        }
        final List<BodyPart> checkedParts = Lists.newArrayList();
        for (final BodyPart.Type neededPart : item.getBodyPartTypes()) {
            for (final BodyPart performersPart : getPerformer().getBodyParts(neededPart)) {
                if (checkedParts.contains(performersPart)) {
                    continue;
                }
                if (!performersPart.getItem().isPresent()) {
                    checkedParts.add(performersPart);
                    break;
                }
                //gameInformation.addMessage("You do not have free hands to wield that!");
                return failure("Not enough hands to wield " + item);
            }
        }
        return none();
    }

    @Override
    protected ActionResult doExecute() {
        for (final BodyPart.Type neededPart : item.getBodyPartTypes()) {
            for (final BodyPart hand : getPerformer().getBodyParts(neededPart)) {
                if (!hand.getItem().isPresent()) {
                    hand.setItem(item);
                    break;
                }
            }
        }
        return new ActionResult(describeAction(getPerformer(), "wield", item));
    }
}
