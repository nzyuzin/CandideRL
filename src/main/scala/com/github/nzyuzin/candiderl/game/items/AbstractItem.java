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

package com.github.nzyuzin.candiderl.game.items;

import com.github.nzyuzin.candiderl.game.AbstractGameObject;
import com.github.nzyuzin.candiderl.game.characters.bodyparts.BodyPart;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.google.common.collect.ImmutableList;

abstract class AbstractItem extends AbstractGameObject implements Item {

    protected final ColoredChar charOnMap;
    protected final int weight;
    protected final int size;

    AbstractItem(String name, String description, ColoredChar onMap, int weight, int size) {
        super(name, description);
        this.charOnMap = onMap;
        this.weight = weight;
        this.size = size;
    }

    public int getWeight() {
        return weight;
    }

    public int getSize() {
        return size;
    }

    public ColoredChar getChar() {
        return charOnMap;
    }

    @Override
    public ImmutableList<BodyPart.Type> getBodyPartTypes() {
        return ImmutableList.of();
    }
}
