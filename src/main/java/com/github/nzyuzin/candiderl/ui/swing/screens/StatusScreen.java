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

package com.github.nzyuzin.candiderl.ui.swing.screens;

import com.github.nzyuzin.candiderl.game.GameInformation;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;

public class StatusScreen extends AbstractDisplayedScreen {

    public StatusScreen(TextWindow gameWindow) {
        super(gameWindow);
    }

    public void show(final GameInformation gameInfo) {
        clearScreen();
        final Player player = gameInfo.getPlayer();
        writeBlackWhiteLine(player.getName() + "   HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        writeBlackWhiteLine("Str: " + player.getStrength());
        writeBlackWhiteLine("Dex: " + player.getDexterity());
        writeBlackWhiteLine("Int: " + player.getIntelligence());
        writeBlackWhiteLine("Armor: " + player.getArmor());
    }
}
