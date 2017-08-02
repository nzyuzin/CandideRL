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

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.fov.FieldOfVision;
import com.github.nzyuzin.candiderl.game.fov.FovFactory;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.google.common.collect.Lists;

import static com.github.nzyuzin.candiderl.game.characters.ItemSlot.Type.AMULET;
import static com.github.nzyuzin.candiderl.game.characters.ItemSlot.Type.BODY;
import static com.github.nzyuzin.candiderl.game.characters.ItemSlot.Type.HAND;
import static com.github.nzyuzin.candiderl.game.characters.ItemSlot.Type.HEAD;
import static com.github.nzyuzin.candiderl.game.characters.ItemSlot.Type.LEGS;
import static com.github.nzyuzin.candiderl.game.characters.ItemSlot.Type.RING;

public final class Player extends AbstractGameCharacter {

    private FieldOfVision fov = null;

    public Player(final String name) {
        super(name, "Yet another wanderer in forgotten land", DEFAULT_HP,
                Lists.newArrayList(new ItemSlot("right hand", HAND), new ItemSlot("left hand", HAND),
                        new ItemSlot("head", HEAD), new ItemSlot("body", BODY), new ItemSlot("legs", LEGS),
                        new ItemSlot("left ring", RING), new ItemSlot("right ring", RING), new ItemSlot("amulet", AMULET)));
        this.charOnMap = ColoredChar .getColoredChar('@');
        fov = FovFactory.getInstance().getFOV(this, GameConfig.VIEW_DISTANCE_LIMIT);
    }

    public ColoredChar[][] getVisibleMap(int width, int height) {
        return fov.getVisibleCells(width, height);
    }

}
