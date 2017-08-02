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

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.GameInformation;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;

import java.util.List;

public class GameScreen extends AbstractDisplayedScreen {

    public GameScreen(TextWindow gameWindow) {
        super(gameWindow);
    }

    public void draw(final GameInformation gameInfo) {
        if (log.isTraceEnabled()) {
            log.trace("drawUi begin");
        }
        getGameWindow().resetCursor();
        final Player player = gameInfo.getPlayer();
        final PositionOnMap playerPosition = player.getPositionOnMap();
        final ColoredChar[][] visibleMap;
        if (!player.isDead()) {
            visibleMap = player.getVisibleMap(getMapWidth(), getMapHeight());
        } else {
            visibleMap = playerPosition.getMap().getVisibleChars(
                    playerPosition.getPosition(), getMapWidth(), getMapHeight());
        }
        final int messagesHeight = GameConfig.DEFAULT_MESSAGES_PANEL_HEIGHT;
        final int screenHeight = getMapHeight() + messagesHeight;
        final List<String> messages = gameInfo.getMessages();

        // drawing begins in upper left corner of screen
        // map passed as argument has (0, 0) as lower left point
        for (int i = screenHeight - 1; i >= 0; i--) {
            if (i < (screenHeight - getMapHeight())) { // map is written
                if (messagesHeight == i + 1) {
                    writeBlackWhite("-", getMapWidth());
                } else if (messages.size() >= i + 1) {
                    writeToMessagesPanel(messages.get(i));
                } else {
                    writeToMessagesPanel("");
                }
            } else {
                for (int j = 0; j < getMapWidth(); j++) { // write map
                    final ColoredChar c = visibleMap[j][i - (screenHeight - getMapHeight())];
                    write(c.getChar(), c.getForeground(), c.getBackground());
                }
            }
            drawStatsPanelRow(gameInfo, screenHeight, i);
        }
        if (log.isTraceEnabled()) {
            log.trace("drawUi end");
        }
    }

    private void drawStatsPanelRow(GameInformation gameInfo, int screenHeight, int mapRow) {
        final GameCharacter player = gameInfo.getPlayer();
        if (mapRow == 0 || mapRow == screenHeight - 1) {
            for (int k = 0; k < GameConfig.DEFAULT_STATS_PANEL_WIDTH; k++) {
                writeBlackWhite('-');
            }
            return;
        }
        writeBlackWhite('|');
        if (mapRow == screenHeight - 2) {
            writeToStatsPanel(player.getName());
            return;
        }
        if (mapRow == screenHeight - 4) {
            final String health = String.format("HP: %d/%d", player.getCurrentHP(), player.getMaxHP());
            writeToStatsPanel(health);
            return;
        }
        if (mapRow == screenHeight - 6) {
            writeToStatsPanel("Current turn: " + gameInfo.getCurrentTurn());
            return;
        }
        if (mapRow == screenHeight - 8) {
            writeToStatsPanel("Depth: " + gameInfo.getDepth());
            return;
        }
        writeToStatsPanel("");
    }

    private void writeToStatsPanel(final String s) {
        if (s.length() > GameConfig.DEFAULT_STATS_PANEL_WIDTH - 1) {
            writeBlackWhite(s.substring(0, GameConfig.DEFAULT_STATS_PANEL_WIDTH - 1));
        } else {
            writeBlackWhite(s);
            writeBlanks((GameConfig.DEFAULT_STATS_PANEL_WIDTH - 1) - s.length());
        }
    }

    private void writeToMessagesPanel(final String s) {
        final int messagesSpaceWidth = getGameWindow().getColumns() - GameConfig.DEFAULT_STATS_PANEL_WIDTH;
        if (s.length() > messagesSpaceWidth) {
            writeBlackWhite(s.substring(0, messagesSpaceWidth));
        } else {
            writeBlackWhite(s);
            writeBlanks(messagesSpaceWidth - s.length());
        }
    }

    private int getMapWidth() {
        return GameConfig.DEFAULT_MAP_WINDOW_WIDTH;
    }

    private int getMapHeight() {
        return GameConfig.DEFAULT_MAP_WINDOW_HEIGHT;
    }
}
