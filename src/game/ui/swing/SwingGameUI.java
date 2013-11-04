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

package game.ui.swing;

import game.ui.GameUI;
import game.utility.ColoredChar;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SwingGameUI implements GameUI {

    private JFrame mainWindow;
    private TextWindow mapWindow;
    private final int DEFAULT_MAP_WIDTH = 160;
    private final int DEFAULT_MAP_HEIGHT = 51;

    private char key;
    private boolean keyRead;

    private final Log log = LogFactory.getLog(SwingGameUI.class);

    public static GameUI getUI() {
        return new SwingGameUI();
    }

    private SwingGameUI() {
        log.trace("SwingGameUI creation start");
        long initTime = System.currentTimeMillis();
        mainWindow = new JFrame("CandideRL");
        mapWindow = TextWindow.getTextWindow(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT);
        log.trace("Created console :: " + (System.currentTimeMillis() - initTime) + "ms");

        mainWindow.setPreferredSize(mapWindow.getPreferredSize());
        mainWindow.getContentPane().add(mapWindow);
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                key = e.getKeyChar();
                keyRead = false;
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        };
        mainWindow.addKeyListener(kl);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    public void drawMap(ColoredChar[][] charMap) {
        log.trace("drawMap begin");
        ColoredChar c;
        for (int i = 0; i < charMap[0].length; i++) {
            for (int j = 0; j < charMap.length; j++) {
                c = charMap[j][i];
                mapWindow.write(c.getChar(), c.getForeground(), c.getBackground());
            }
        }
        mapWindow.repaint();
        log.trace("drawMap end");
    }

    public char getInputChar() {
        if (keyRead) return '\n';
        keyRead = true;
        return key;
    }

    public void showAnnouncement(String msg) {
    }

    public void showMessage(String msg) {
    }

    public void showStats(String stats) {
    }

    public void close() {
        log.trace("SwingGameUI close begin");
        mainWindow.setVisible(false);
        mainWindow.dispose();
        log.trace("SwingGameUI close end");
    }

    public int getScreenWidth() {
        return DEFAULT_MAP_WIDTH;
    }

    public int getScreenHeight() {
        return DEFAULT_MAP_HEIGHT;
    }

    public int getMapWidth() {
        return DEFAULT_MAP_WIDTH;
    }

    public int getMapHeight() {
        return DEFAULT_MAP_HEIGHT;
    }
}
