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

package com.github.nzyuzin.candiderl.game.characters.bodyparts;

import com.github.nzyuzin.candiderl.game.items.Item;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.Optional;

public class Hand extends BodyPart {

    private Item ring;

    public Hand(String name) {
        super(name, Type.HAND);
        this.ring = null;
    }

    public Hand() {
        this("hand");
    }

    public Optional<Item> getRing() {
        return Optional.ofNullable(ring);
    }

    public void setRing(@Nonnull Item ring) {
        Preconditions.checkNotNull(ring);
        this.ring = ring;
    }

    public void removeRing() {
        this.ring = null;
    }
}
