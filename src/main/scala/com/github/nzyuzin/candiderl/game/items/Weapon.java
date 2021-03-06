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

import com.github.nzyuzin.candiderl.game.characters.bodyparts.BodyPart;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.google.common.collect.ImmutableList;

public class Weapon extends AbstractItem {

    public enum Type {

        ONE_HANDED(ImmutableList.of(BodyPart.Type.HAND)),
        TWO_HANDED(ImmutableList.of(BodyPart.Type.HAND, BodyPart.Type.HAND));

        Type(ImmutableList<BodyPart.Type> bodyPartTypes) {
            this.bodyPartTypes = bodyPartTypes;
        }

        private ImmutableList<BodyPart.Type> bodyPartTypes;

        public ImmutableList<BodyPart.Type> getBodyPartTypes() {
            return bodyPartTypes;
        }
    }

    private final Type type;
    private final int damage;

    public Weapon(String name, String description, Type type, int damage, int weight, int size) {
        super(name, description, ColoredChar.getColoredChar('/'), weight, size);
        this.type = type;
        this.damage = damage;
    }

    @Override
    public ImmutableList<BodyPart.Type> getBodyPartTypes() {
        return type.getBodyPartTypes();
    }

    public Type getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}
