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

import com.github.nzyuzin.candiderl.game.utility.ColoredChar;

import java.awt.Color;
import java.util.Random;

public class NpcFactory {

    private Random random;

    public NpcFactory(final Random random) {
        this.random = random;
    }

    public Npc getNpc() {
        final int roll = random.nextInt(2);
        if (roll == 0) {
            return new Npc("Troll", "A furious beast with sharp claws.", Races.TROLL.get(),
                    ColoredChar.getColoredChar('t', ColoredChar.YELLOW));
        } else if (roll == 1) {
            return new Npc("Goblin", "A small green humanoid. Apparently not very strong.", Races.GOBLIN.get(),
                    ColoredChar.getColoredChar('g', ColoredChar.GREEN));
        } else {
            return new Npc("Rat", "A small rodent. It doesn't seem to like you.", Races.RODENT.get(),
                    ColoredChar.getColoredChar('r', Color.ORANGE));
        }
    }

}
