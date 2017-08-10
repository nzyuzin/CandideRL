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
import com.github.nzyuzin.candiderl.game.events.ExplosionEvent;
import com.github.nzyuzin.candiderl.game.events.PositionedEventContext;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;

public class CastExplosionAction extends AbstractGameAction {
    private final PositionOnMap position;

    public CastExplosionAction(GameCharacter subject, PositionOnMap position) {
        super(subject);
        this.position = position;
    }

    @Override
    public Optional<String> failureReason() {
        return none();
    }

    @Override
    protected ActionResult doExecute() {
        PositionedEventContext context = new PositionedEventContext(position);
        return new ActionResult(new ExplosionEvent(context, 3, 10));
    }
}
