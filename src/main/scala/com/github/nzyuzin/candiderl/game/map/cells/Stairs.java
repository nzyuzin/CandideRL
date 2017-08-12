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
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class Stairs extends AbstractMapCell {
    public enum Type {
        UP('<'), DOWN('>');

        private final char chr;

        Type(char chr) {
            this.chr = chr;
        }

        @Override
        public String toString() {
            switch (this) {
                case DOWN:
                    return "downwards";
                case UP:
                    return "upwards";
                default:
                    return "<error>";
            }
        }

        public char getChar() {
            return this.chr;
        }
    }

    private final Type type;
    private Optional<PositionOnMap> matchingPosition;

    public Stairs(final Type type) {
        super("Stairs", "Stone stairs leading " + type, ColoredChar.getColoredChar(type.getChar()), true, true);
        this.type = type;
        this.matchingPosition = Optional.absent();
    }

    public Type getType() {
        return type;
    }

    public Optional<PositionOnMap> getMatchingPosition() {
        return matchingPosition;
    }

    public void setMatchingStairs(final PositionOnMap matchingPosition) {
        Preconditions.checkArgument(!this.matchingPosition.isPresent(), "Matching position is already set!");
        this.matchingPosition = Optional.of(matchingPosition);
    }
}
