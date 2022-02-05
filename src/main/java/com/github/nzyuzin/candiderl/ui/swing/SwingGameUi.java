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

package com.github.nzyuzin.candiderl.ui.swing;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.GameObject;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.ui.GameUi;
import com.github.nzyuzin.candiderl.ui.swing.screens.AnnouncementScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.ExamineScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.GameScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.InventoryScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.ItemScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.MenuScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.StatusScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.TextEnterScreen;
import com.github.nzyuzin.candiderl.ui.swing.screens.ViewMapScreen;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class SwingGameUi implements GameUi {
    private final JFrame mainWindow;
    private final TextWindow gameWindow;

    private final GameScreen gameScreen;
    private final ViewMapScreen viewMapScreen;
    private final ExamineScreen examineScreen;
    private final AnnouncementScreen announcementScreen;
    private final MenuScreen menuScreen;
    private final StatusScreen statusScreen;
    private final InventoryScreen inventoryScreen;
    private final ItemScreen itemScreen;
    private final TextEnterScreen textEnterScreen;

    private Character key;
    private boolean keyRead = true;
    private final Object lock = new Object();

    public SwingGameUi(String frameName) {
        mainWindow = new JFrame(frameName);
        gameWindow = TextWindow.getTextWindow(GameConfig.DEFAULT_MAP_WINDOW_WIDTH + GameConfig.DEFAULT_STATS_PANEL_WIDTH,
                        GameConfig.DEFAULT_MAP_WINDOW_HEIGHT + GameConfig.DEFAULT_MESSAGES_PANEL_HEIGHT);
        mainWindow.getContentPane().add(gameWindow);
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

        gameScreen = new GameScreen(gameWindow, GameConfig.DEFAULT_MAP_WINDOW_WIDTH, GameConfig.DEFAULT_MAP_WINDOW_HEIGHT,
                GameConfig.DEFAULT_MESSAGES_PANEL_HEIGHT, GameConfig.DEFAULT_STATS_PANEL_WIDTH);
        viewMapScreen = new ViewMapScreen(gameWindow);
        examineScreen = new ExamineScreen(gameWindow);
        announcementScreen = new AnnouncementScreen(gameWindow);
        menuScreen = new MenuScreen(gameWindow);
        statusScreen = new StatusScreen(gameWindow);
        inventoryScreen = new InventoryScreen(gameWindow);
        itemScreen = new ItemScreen(gameWindow);
        textEnterScreen = new TextEnterScreen(gameWindow);
    }

    @Override
    public void init() {
        gameWindow.init();
        mainWindow.setPreferredSize(gameWindow.getPreferredSize());
        mainWindow.setResizable(false);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    @Override
    public void drawGame(final GameState gameInfo) {
        gameScreen.draw(gameInfo);
        gameWindow.repaint();
    }

    @Override
    public void drawMapView(Map map, Position position) {
        viewMapScreen.draw(map, position);
        gameWindow.repaint();
    }

    @Override
    public void drawExamineScreen(GameObject object) {
        examineScreen.draw(object);
        gameWindow.repaint();
    }

    @Override
    public void showStatus(final GameState gameInfo) {
        statusScreen.show(gameInfo);
        gameWindow.repaint();
    }

    @Override
    public void showInventory(GameState gameInfo) {
        inventoryScreen.draw(gameInfo);
        gameWindow.repaint();
    }

    @Override
    public void showItem(Item item) {
        itemScreen.draw(item);
        gameWindow.repaint();
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
    public void showAnnouncement(String msg) {
        announcementScreen.draw(msg);
        gameWindow.repaint();
        while (getInputChar() != ' ') { /* wait for space */ }
    }

    @Override
    public void displayMenu(List<? extends Object> options) {
        menuScreen.drawOptions(options);
        gameWindow.repaint();
    }

    @Override
    public void drawInputForm(String formQuery, int length, String enteredCharacters) {
        textEnterScreen.drawInputForm(formQuery, length, enteredCharacters);
        gameWindow.repaint();
    }

    @Override
    public void close() {
        mainWindow.setVisible(false);
        mainWindow.dispose();
    }
}
