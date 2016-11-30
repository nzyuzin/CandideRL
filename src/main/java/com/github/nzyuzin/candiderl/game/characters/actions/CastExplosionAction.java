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
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.events.ExplosionEvent;
import com.github.nzyuzin.candiderl.game.events.PositionedEventContext;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;

import java.util.Collections;
import java.util.List;

public class CastExplosionAction extends AbstractGameAction {
    private final PositionOnMap position;

    public CastExplosionAction(GameCharacter subject, PositionOnMap position) {
        super(subject);
        this.position = position;
    }

    @Override
    public boolean canBeExecuted() {
        return true;
    }

    @Override
    protected List<Event> doExecute() {
        PositionedEventContext context = new PositionedEventContext(position);
        return Collections.singletonList(new ExplosionEvent(context, 3, 10));
    }
}
