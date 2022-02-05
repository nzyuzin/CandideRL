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

import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;

import java.awt.Color;

public class ViewMapScreen extends AbstractDisplayedScreen {

    public ViewMapScreen(TextWindow gameWindow) {
        super(gameWindow);
    }

    public void draw(final Map map, final Position viewPoint) {
        clearScreen();
        final int mapViewWidth = getGameWindow().getColumns();
        final int mapViewHeight = getGameWindow().getRows() - 1;
        final ColoredChar[][] visibleMap = map.getVisibleChars(viewPoint, mapViewWidth, mapViewHeight);
        // drawing begins in upper left corner of screen
        // map passed as argument has (0, 0) as lower left point
        for (int i = mapViewHeight - 1; i >= 0; i--) {
            for (int j = 0; j < mapViewWidth; j++) {
                if (i == mapViewHeight / 2 && j == mapViewWidth / 2) {
                    write('X', Color.WHITE, Color.YELLOW);
                } else {
                    final ColoredChar c = visibleMap[j][i];
                    write(c.getChar(), c.getForeground(), c.getBackground());
                }
            }
        }
        writeBottomRow("Press <esc> to return to normal mode");
    }
}
