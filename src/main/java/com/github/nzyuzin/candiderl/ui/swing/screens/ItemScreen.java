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

import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.utility.KeyDefinitions;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;

import java.util.HashMap;
import java.util.Map;

public class ItemScreen extends AbstractDisplayedScreen {

    private static final Map<Character, String> itemActions;

    static {
        itemActions = new HashMap<>();
        itemActions.put(KeyDefinitions.DROP_ITEM_KEY, "Drop");
        itemActions.put(KeyDefinitions.WIELD_ITEM_KEY, "Wield");
    }

    public ItemScreen(TextWindow gameWindow) {
        super(gameWindow);
    }

    public void draw(Item item) {
        clearScreen();
        writeBlackWhiteLine(item.getName());
        nextLine();
        writeBlackWhiteLine(item.getDescription());
        nextLine();
        writeBlackWhiteLine("Weight: " + item.getWeight() + "   Size: " + item.getSize());

        writeBeforeBottomRow(actionRow(itemActions));
        writeBottomRow("Press <esc> to exit");
    }
}
