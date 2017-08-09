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

package com.github.nzyuzin.candiderl.game.characters;

import com.github.nzyuzin.candiderl.game.AbstractGameObject;
import com.google.common.collect.Lists;

import java.util.List;

public class Race extends AbstractGameObject implements HasAttributes {

    private final List<BodyPart> bodyParts;
    private final Attributes attributes;

    public Race(String name, String description, List<BodyPart> bodyParts, Attributes attributes) {
        super(name, description);
        this.bodyParts = bodyParts;
        this.attributes = attributes;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    public List<BodyPart> getBodyParts() {
        return Lists.newArrayList(bodyParts);
    }

}
