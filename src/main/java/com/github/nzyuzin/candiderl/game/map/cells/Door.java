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

package com.github.nzyuzin.candiderl.game.map.cells;

import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

import java.awt.Color;

public class Door extends AbstractMapCell {

    private static final ColoredChar OPEN_CHAR = ColoredChar.getColoredChar('|', Color.ORANGE);
    private static final ColoredChar CLOSED_CHAR = ColoredChar.getColoredChar('+', Color.ORANGE);

    private boolean isOpen;

    public Door(String name, String desc, ColoredChar onMap, boolean transp, boolean isPassable, boolean isOpen) {
        super(name, desc, onMap, transp, isPassable);
        this.isOpen = isOpen;
    }

    public Door() {
        this("Door", "A regular wooden door", CLOSED_CHAR, false, false, false);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isClosed() {
        return !isOpen;
    }

    public void open() {
        isOpen = true;
        isPassable = true;
        transparent = true;
        charOnMap = OPEN_CHAR;
    }

    public void close() {
        isOpen = false;
        isPassable = false;
        transparent = false;
        charOnMap = CLOSED_CHAR;
    }
}
