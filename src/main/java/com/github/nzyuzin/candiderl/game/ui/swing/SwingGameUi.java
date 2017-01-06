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

package com.github.nzyuzin.candiderl.game.ui.swing;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.ui.GameUi;
import com.github.nzyuzin.candiderl.game.ui.VisibleInformation;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingGameUi implements GameUi {
    private JFrame mainWindow;
    private TextWindow mapWindow;

    private Character key;
    private boolean keyRead = true;
    private final Object lock = new Object();

    private static final Logger log = LoggerFactory.getLogger(SwingGameUi.class);

    public static GameUi getUi(String windowName) {
        return new SwingGameUi(windowName);
    }

    private SwingGameUi(String frameName) {
        mainWindow = new JFrame(frameName);
        mapWindow = TextWindow.getTextWindow(GameConfig.DEFAULT_MAP_WINDOW_WIDTH + GameConfig.DEFAULT_STATS_PANEL_WIDTH,
                        GameConfig.DEFAULT_MAP_WINDOW_HEIGHT);
        mainWindow.getContentPane().add(mapWindow);
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                synchronized (lock) {
                    key = e.getKeyChar();
                    keyRead = false;
                    lock.notify();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        };
        mainWindow.addKeyListener(kl);
    }

    @Override
    public void init() {
        mapWindow.init();
        mainWindow.setPreferredSize(mapWindow.getPreferredSize());
        mainWindow.setResizable(false);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    @Override
    public void drawUi(final VisibleInformation uiInfo) {
        if (log.isTraceEnabled()) {
            log.trace("drawUi begin");
        }
        final ColoredChar[][] charMap = uiInfo.getVisibleMap();
        final int mapHeight = charMap[0].length;
        final int mapWidth = charMap.length;
        // drawing begins in upper left corner of screen
        // map passed as argument has (0, 0) as lower left point
        for (int i = mapHeight - 1; i >= 0; i--) {
            for (int j = 0; j < mapWidth; j++) {
                ColoredChar c = charMap[j][i];
                mapWindow.write(c.getChar(), c.getForeground(), c.getBackground());
            }
            drawStatsPanelRow(uiInfo, mapHeight, i);
        }
        mapWindow.repaint();
        if (log.isTraceEnabled()) {
            log.trace("drawUi end");
        }
    }

    private void drawStatsPanelRow(VisibleInformation uiInfo, int mapHeight, int mapRow) {
        final GameCharacter player = uiInfo.getPlayer();
        if (mapRow == 0 || mapRow == mapHeight - 1) {
            for (int k = 0; k < GameConfig.DEFAULT_STATS_PANEL_WIDTH; k++) {
                writeBlackWhite('-');
            }
            return;
        }
        writeBlackWhite('|');
        if (mapRow == mapHeight - 2) {
            writeToStatsPanel(player.getName());
            return;
        }
        if (mapRow == mapHeight - 4) {
            final String health = String.format("HP: %d/%d", player.getCurrentHP(), player.getMaxHP());
            writeToStatsPanel(health);
            return;
        }
        if (mapRow == mapHeight - 6) {
            writeToStatsPanel("Current turn: " + uiInfo.getCurrentTurn());
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

    private void writeBlanks(final int times) {
        for (int i = 0; i < times; i++) {
            writeBlackWhite(" ");
        }
    }

    private void writeBlackWhite(final String s) {
        for (int i = 0; i < s.length(); i++) {
            writeBlackWhite(s.charAt(i));
        }
    }

    private void writeBlackWhite(final char character) {
        mapWindow.write(character, Color.WHITE, Color.BLACK);
    }

    @Override
    public char getInputChar() {
        synchronized (lock) {
            try {
                while (keyRead) {
                    lock.wait();
                }
                keyRead = true;
                return key;
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

    @Override
    public void showAnnouncement(String msg) { }

    @Override
    public void close() {
        mainWindow.setVisible(false);
        mainWindow.dispose();
    }

    @Override
    public int getMapWidth() {
        return GameConfig.DEFAULT_MAP_WINDOW_WIDTH;
    }

    @Override
    public int getMapHeight() {
        return GameConfig.DEFAULT_MAP_WINDOW_HEIGHT;
    }
}
