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

import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;
import com.google.common.collect.ImmutableList;

public class InventoryScreen extends AbstractDisplayedScreen {

    public InventoryScreen(TextWindow gameWindow) {
        super(gameWindow);
    }

    public void draw(final GameState gameInfo) {
        final ImmutableList<Item> items = gameInfo.getPlayer().getItems();
        int remainingItems = items.size();
        while (remainingItems > 0) {
            clearScreen();
            for (int i = 0; i < items.size() && i < getGameWindow().getRows() - 1; i++) {
                final char index = (char)('a' + i);
                writeBlackWhiteLine(index + ") " + items.get(i));
                remainingItems--;
            }
            if (remainingItems > 0) {
                writeBottomRow("Press any key to continue");
            } else {
                writeBottomRow("Press <esc> to exit");
            }
        }
    }
}
