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

package com.github.nzyuzin.candiderl.game.map.cells.effects;

import com.github.nzyuzin.candiderl.game.map.cells.AbstractMapCell;
import com.google.common.base.Preconditions;

public abstract class AbstractMapCellEffect<T extends AbstractMapCell> implements MapCellEffect<T> {
    private int duration;

    public AbstractMapCellEffect(int initialDuration) {
        this.duration = initialDuration;
    }

    @Override
    public void apply(T cell) {
        Preconditions.checkArgument(duration > 0, "Can only apply effects with positive duration");
        duration--;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
