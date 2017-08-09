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

import com.google.common.collect.Lists;

public enum Races {

    HUMAN(new Race("Human", "Human", Lists.newArrayList(
            new BodyPart("head", BodyPart.Type.HEAD),
            new BodyPart("chest", BodyPart.Type.CHEST),
            new BodyPart("neck", BodyPart.Type.NECK),
            new BodyPart("left arm", BodyPart.Type.ARM),
            new BodyPart("right arm", BodyPart.Type.ARM),
            new BodyPart("left hand", BodyPart.Type.HAND),
            new BodyPart("right hand", BodyPart.Type.HAND),
            new BodyPart("left leg", BodyPart.Type.LEG),
            new BodyPart("right leg", BodyPart.Type.LEG),
            new BodyPart("left foot", BodyPart.Type.FOOT),
            new BodyPart("right foot", BodyPart.Type.FOOT)
    ), new Attributes(100, 8, 8, 8, 0))),

    TROLL(new Race("Troll", "Troll", Lists.newArrayList(
            new BodyPart("head", BodyPart.Type.HEAD),
            new BodyPart("chest", BodyPart.Type.CHEST),
            new BodyPart("neck", BodyPart.Type.NECK),
            new BodyPart("left arm", BodyPart.Type.ARM),
            new BodyPart("right arm", BodyPart.Type.ARM),
            new BodyPart("left hand", BodyPart.Type.HAND),
            new BodyPart("right hand", BodyPart.Type.HAND),
            new BodyPart("left leg", BodyPart.Type.LEG),
            new BodyPart("right leg", BodyPart.Type.LEG),
            new BodyPart("left foot", BodyPart.Type.FOOT),
            new BodyPart("right foot", BodyPart.Type.FOOT)
    ), new Attributes(100, 12, 7, 5, 3))),

    GOBLIN(new Race("Goblin", "Goblin", Lists.newArrayList(
            new BodyPart("head", BodyPart.Type.HEAD),
            new BodyPart("chest", BodyPart.Type.CHEST),
            new BodyPart("neck", BodyPart.Type.NECK),
            new BodyPart("left arm", BodyPart.Type.ARM),
            new BodyPart("right arm", BodyPart.Type.ARM),
            new BodyPart("left hand", BodyPart.Type.HAND),
            new BodyPart("right hand", BodyPart.Type.HAND),
            new BodyPart("left leg", BodyPart.Type.LEG),
            new BodyPart("right leg", BodyPart.Type.LEG),
            new BodyPart("left foot", BodyPart.Type.FOOT),
            new BodyPart("right foot", BodyPart.Type.FOOT)
    ), new Attributes(70, 7, 7, 5, 0))),

    RODENT(new Race("Rodent", "Rodent", Lists.newArrayList(
            new BodyPart("head", BodyPart.Type.HEAD),
            new BodyPart("chest", BodyPart.Type.CHEST),
            new BodyPart("neck", BodyPart.Type.NECK),
            new BodyPart("left arm", BodyPart.Type.ARM),
            new BodyPart("right arm", BodyPart.Type.ARM),
            new BodyPart("left hand", BodyPart.Type.HAND),
            new BodyPart("right hand", BodyPart.Type.HAND),
            new BodyPart("left leg", BodyPart.Type.LEG),
            new BodyPart("right leg", BodyPart.Type.LEG),
            new BodyPart("left foot", BodyPart.Type.FOOT),
            new BodyPart("right foot", BodyPart.Type.FOOT)
    ), new Attributes(20, 4, 6, 2, 0)));

    private Race race;

    Races(Race race) {
        this.race = race;
    }

    public Race get() {
        return this.race;
    }
}
