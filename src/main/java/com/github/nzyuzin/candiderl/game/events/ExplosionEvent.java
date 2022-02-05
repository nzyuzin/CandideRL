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

package com.github.nzyuzin.candiderl.game.events;

import com.github.nzyuzin.candiderl.game.map.cells.effects.Explosion;
import com.github.nzyuzin.candiderl.game.utility.Position;

public class ExplosionEvent extends AbstractEvent<PositionedEventContext> {
    private final int size;
    private final int damage;

    public ExplosionEvent(PositionedEventContext context, int size, int damage) {
        super(context);
        this.size = size;
        this.damage = damage;
    }

    @Override
    public void occur() {
        final Position pos = getContext().getPosition();
        getContext().getMap().addEffectsToArea(pos, size, size, new Explosion(damage));
    }
}
