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

public class Explosion extends AbstractMapCellEffect<AbstractMapCell> {
    private final int explosionPower;

    public Explosion(int explosionPower) {
        super(1);
        Preconditions.checkArgument(explosionPower >= 0, "Explosion power should be non-negative!");
        this.explosionPower = explosionPower;
    }

    @Override
    public void apply(AbstractMapCell cell) {
        super.apply(cell);
        cell.getGameCharacter().takeDamage(explosionPower);
    }
}
