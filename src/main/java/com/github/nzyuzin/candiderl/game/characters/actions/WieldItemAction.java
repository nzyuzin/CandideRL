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
import com.github.nzyuzin.candiderl.game.events.AbstractEventContext;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

public class WieldItemAction extends AbstractGameAction {

    private final Item item;

    public WieldItemAction(GameCharacter subject, Item item) {
        super(subject);
        this.item = item;
    }

    @Override
    public boolean canBeExecuted() {
        Preconditions.checkArgument(getPerformer().getItems().contains(item), "Item should be in the inventory!");
        for (final BodyPart.Type bodyPart : item.getBodyPartTypes()) {
            Preconditions.checkArgument(bodyPart == BodyPart.Type.HAND, "Cannot wield " + item + " in hands");
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
                return false; // we cannot find a free part to hold the item
            }
        }
        return true;
    }

    @Override
    protected List<Event> doExecute() {
        return Lists.newArrayList(new Event<AbstractEventContext>() {
            @Override
            public void occur() {
                for (final BodyPart.Type neededPart : item.getBodyPartTypes()) {
                    for (final BodyPart hand : getPerformer().getBodyParts(neededPart)) {
                        if (!hand.getItem().isPresent()) {
                            hand.setItem(item);
                            break;
                        }
                    }
                }
            }

            @Override
            public String getTextualDescription() {
                return "You wield " + item;
            }
        });
    }
}
