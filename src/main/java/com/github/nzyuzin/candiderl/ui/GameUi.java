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

package com.github.nzyuzin.candiderl.ui;

import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.Position;

import java.util.List;

public interface GameUi extends AutoCloseable {
    void init();
    char getInputChar();
    void drawGame(GameState gameInfo);
    void drawMessages(GameState gameState);
    void drawMapView(GameState gameState, Map map, Position position);
    void drawExamineScreen(GameObject object);
    void showStatus(GameState gameInfo);
    void showInventory(GameState gameInfo);
    void showItem(Item item);
    void showAnnouncement(String msg);
    void displayMenu(List<? extends Object> options);
    void drawInputForm(final String formQuery, final int length, final String enteredCharacters);
}
