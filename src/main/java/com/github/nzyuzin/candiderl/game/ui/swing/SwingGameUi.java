/*
 *  This file is part of CandideRL.
 *
 *  CandideRL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CandideRL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game.ui.swing;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.ui.GameUi;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingGameUi implements GameUi {

    private JFrame mainWindow;
    private TextWindow mapWindow;

    private Character key;
    private boolean keyRead = true;
    private final Object lock = new Object();

    private static final Logger log = LoggerFactory.getLogger(SwingGameUi.class);

    public static GameUi getUi() {
        return new SwingGameUi();
    }

    private SwingGameUi() {
        long initTime = 0;
        if (log.isTraceEnabled()) {
            log.trace("SwingGameUI creation start");
            initTime = System.currentTimeMillis();
        }
        mainWindow = new JFrame("CandideRL");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mapWindow = TextWindow
                .getTextWindow(GameConfig.DEFAULT_MAP_WINDOW_WIDTH, GameConfig.DEFAULT_MAP_WINDOW_HEIGHT, screenSize);
        if (log.isTraceEnabled()) {
            log.trace(String.format("Created console :: %dms", System.currentTimeMillis() - initTime));
            log.trace(String.format("mapWindow preferred = %s%n mapWindow minimum = %s%n mapWindow maximum = %s",
                    mapWindow.getPreferredSize(), mapWindow.getMinimumSize(), mapWindow.getMaximumSize()));
        }
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
        mainWindow.setPreferredSize(mapWindow.getPreferredSize());
        mainWindow.setResizable(false);
        mainWindow.addKeyListener(kl);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    @Override
    public void drawMap(ColoredChar[][] charMap) {
        if (log.isTraceEnabled()) {
            log.trace("drawMap begin");
        }
        // drawing begins in upper left corner of screen
        // map passed as argument has (0, 0) as lower left point
        for (int i = charMap[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < charMap.length; j++) {
                ColoredChar c = charMap[j][i];
                mapWindow.write(c.getChar(), c.getForeground(), c.getBackground());
            }
        }
        mapWindow.repaint();
        if (log.isTraceEnabled()) {
            log.trace("drawMap end");
        }
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
    public void showMessage(String msg) { }

    @Override
    public void showStats(String stats) { }

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
