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
import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.fov.FieldOfVision;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.ui.swing.TextWindow;

import java.util.List;

public class GameScreen extends AbstractDisplayedScreen {

    private final int mapWidth;
    private final int mapHeight;
    private final int messagesHeight;
    private final int statsWidth;

    public GameScreen(TextWindow gameWindow, int mapWidth, int mapHeight, int messagesHeight, int statsWidth) {
        super(gameWindow);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.messagesHeight = messagesHeight;
        this.statsWidth = statsWidth;
    }

    public void draw(final GameState gameInfo) {
        if (log.isTraceEnabled()) {
            log.trace("drawUi begin");
        }
        getGameWindow().resetCursor();
        final Player player = gameInfo.getPlayer();
        final Position playerPosition = player.getPosition();
        final ColoredChar[][] visibleMap;
        if (!player.isDead()) {
            if (GameConfig.CALCULATE_FIELD_OF_VIEW) {
                final FieldOfVision fov = gameInfo.getGameFactories().getFovFactory().getFov();
                boolean[][] seen =
                        fov.calculateSeenMask(player, GameConfig.VIEW_DISTANCE_LIMIT);
                visibleMap = player.getMap().getVisibleChars(playerPosition, mapWidth, mapHeight, seen);
            } else {
                visibleMap = player.getMap().getVisibleChars(playerPosition, mapWidth, mapHeight);
            }
        } else {
            visibleMap = player.getMap().getVisibleChars(playerPosition, mapWidth, mapHeight);
        }
        final int screenHeight = mapHeight + messagesHeight;
        final List<String> messages = gameInfo.getMessages();

        // drawing begins in upper left corner of screen
        // map passed as argument has (0, 0) as lower left point
        for (int i = screenHeight - 1; i >= 0; i--) {
            if (i < (screenHeight - mapHeight)) { // map is written
                if (messagesHeight == i + 1) {
                    writeBlackWhite("-", mapWidth);
                } else if (messages.size() >= i + 1) {
                    writeToMessagesPanel(messages.get(i));
                } else {
                    writeToMessagesPanel("");
                }
            } else {
                for (int j = 0; j < mapWidth; j++) { // write map
                    final ColoredChar c = visibleMap[j][i - (screenHeight - mapHeight)];
                    write(c.getChar(), c.getForeground(), c.getBackground());
                }
            }
            drawStatsPanelRow(gameInfo, screenHeight, i);
        }
        if (log.isTraceEnabled()) {
            log.trace("drawUi end");
        }
    }

    private void drawStatsPanelRow(GameState gameInfo, int screenHeight, int mapRow) {
        final GameCharacter player = gameInfo.getPlayer();
        if (mapRow == 0 || mapRow == screenHeight - 1) {
            for (int k = 0; k < statsWidth; k++) {
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
            final String health = String.format("HP: %d/%d", player.getCurrentHp(), player.getMaxHp());
            writeToStatsPanel(health);
            return;
        }
        if (mapRow == screenHeight - 6) {
            writeToStatsPanel("Current turn: " + gameInfo.getCurrentTurn());
            return;
        }
        if (mapRow == screenHeight - 8) {
            writeToStatsPanel("Map: " + gameInfo.getPlayer().getMap().getName());
            return;
        }
        writeToStatsPanel("");
    }

    private void writeToStatsPanel(final String s) {
        if (s.length() > statsWidth - 1) {
            writeBlackWhite(s.substring(0, statsWidth - 1));
        } else {
            writeBlackWhite(s);
            writeBlanks((statsWidth - 1) - s.length());
        }
    }

    private void writeToMessagesPanel(final String s) {
        final int messagesSpaceWidth = getGameWindow().getColumns() - statsWidth;
        if (s.length() > messagesSpaceWidth) {
            writeBlackWhite(s.substring(0, messagesSpaceWidth));
        } else {
            writeBlackWhite(s);
            writeBlanks(messagesSpaceWidth - s.length());
        }
    }
}
