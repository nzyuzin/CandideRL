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

package game.map.cells;

import game.utility.ColoredChar;
import game.utility.VisibleCharacters;

public class Floor extends AbstractMapCell {
    private Floor() {
        super("Floor",
                "Rough rock floor.",
                ColoredChar.getColoredChar(VisibleCharacters.FLOOR.getVisibleChar()),
                true,
                true
        );
    }

    public static Floor getFloor() {
        return new Floor();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Floor && super.equals(object);
    }
}
