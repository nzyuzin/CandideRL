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

package game;

import game.ai.ArtificialIntelligence;
import game.characters.NPC;
import game.characters.Player;
import game.map.Map;
import game.map.MapFactory;
import game.ui.GameUI;
import game.ui.swing.SwingGameUI;
import game.utility.ColoredChar;
import game.utility.Direction;
import game.utility.KeyDefinitions;
import game.utility.exceptions.GameClosedException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.ArrayList;
import java.util.Iterator;

public final class GameEngine implements AutoCloseable {

    private static final Logger log = Logger.getRootLogger();

    public static void main(String args[]) {
        try (GameEngine engine = getGameEngine()) {
            engine.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Player player;
    private ArrayList<NPC> npcs;
    private MessageLog messageLog;
    private int currentTurn = 0;
    private GameUI UI;
    private ArtificialIntelligence ai;

    public static GameEngine getGameEngine() {
        return new GameEngine();
    }

    private GameEngine() {

        initLog();

        UI = SwingGameUI.getUI();
        MapFactory mapFactory = MapFactory.getInstance();
        mapFactory.setScreenSize(UI.getScreenWidth(), UI.getScreenHeight());
        mapFactory.setMapSize(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT);
        Map currentMap = mapFactory.getMap();

        if (log.isTraceEnabled()) {
            log.trace("mapFactory done");
        }

        npcs = new ArrayList<>();

        player = Player.getInstance();

        // Some magic constants here
        ai = new ArtificialIntelligence(player, (UI.getMapWidth() + UI.getMapHeight()) / 2 + 100);

        if (GameConfig.SPAWN_MOBS) {
            npcs.add(new NPC(
                    "troll",
                    "A furious beast with sharp claws.",
                    ColoredChar.getColoredChar('t', ColoredChar.RED))
            );
            npcs.add(new NPC(
                    "goblin",
                    "A regular goblin.",
                    ColoredChar.getColoredChar('g', ColoredChar.GREEN))
            );
            for (NPC mob : npcs)
                currentMap.putGameCharacter(mob, currentMap.getRandomFreePosition());
        }
        currentMap.putGameCharacter(player, currentMap.getRandomFreePosition());

        currentTurn = 0;
    }

    private void initLog() {
        log.addAppender(new ConsoleAppender(new PatternLayout(GameConfig.LOG_CONVERSION_PATTERN), ConsoleAppender.SYSTEM_OUT));
        log.setLevel(Level.OFF);
    }

    private void processActions() {
        player.performAction();
/*        for (NPC npc : npcs.toArray(new NPC[npcs.size()])) {
            if (npc.isDead()) {
                npcs.remove(npc);
                continue;
            }
            else ai.chooseAction(npc);
            if (npc.canPerformAction())
                npc.performAction();
            else npc.removeCurrentAction();
        }*/

        NPC npc;
        for (Iterator<NPC> iterator = npcs.iterator(); iterator.hasNext(); ) {
            npc = iterator.next();
            if (npc.isDead()) {
                iterator.remove();
                continue;
            } else ai.chooseAction(npc);
            if (npc.canPerformAction())
                npc.performAction();
            else npc.removeCurrentAction();
        }
    }

    private void advanceTime() {
        if (log.isTraceEnabled()) {
            log.trace("advanceTime begin");
        }
        currentTurn++;
        processActions();
        drawMap();
        showStats();
        if (log.isTraceEnabled()) {
            log.trace(String.format("advanceTime end :: currentTurn = %d", currentTurn));
        }
    }

    private void handleInput() throws GameClosedException {
        char input = UI.getInputChar();
        if (log.isDebugEnabled()) {
            log.debug(String.format("handleInput input = %s", input));
        }
        if (KeyDefinitions.isDirectionKey(input)) {
            ai.chooseActionInDirection(player, Direction.getDirection(input));
            return;
        }
        if (KeyDefinitions.isSkipTurnChar(input)) {
            advanceTime();
            return;
        }
        if (KeyDefinitions.isExitChar(input)) {
            throw new GameClosedException();
        }
    }

    public void close() {
        UI.showAnnouncement("You're leaving the game.");
        try {
            UI.close();
        } catch (Exception ex) {
            throw new RuntimeException("Exception during attempt to close the game");
        }
    }

    private void drawMap() {
        long initTime = 0;
        if (log.isTraceEnabled()) {
            log.trace("drawMap start");
            initTime = System.currentTimeMillis();
        }
        if (!player.isDead()) {
            UI.drawMap(player.getVisibleMap());
        }
        if (log.isTraceEnabled()) {
            log.trace(String.format("drawMap end :: time=%d", System.currentTimeMillis() - initTime));
        }
    }

    private void showStats() {
        UI.showStats(String.format("%s%nCurrent turn: %d%n", player.getStats(), currentTurn));
    }

    public void play() {
        if (log.isTraceEnabled()) {
            log.trace("Game starts");
        }
        UI.showMessage("Prepare to play!");
        drawMap();
        showStats();
        try {
            while (!player.isDead()) {
                handleInput();
                if (player.canPerformAction())
                    advanceTime();
            }
        } catch (GameClosedException gameClosedException) {
            if (log.isDebugEnabled()) {
                log.debug("Game was closed");
            }
        } finally {
            close();
            GameConfig.write();
        }
        if (npcs.isEmpty()) UI.showAnnouncement("All mobs are dead!");
        if (player.isDead()) UI.showAnnouncement("You're dead!");
        if (log.isTraceEnabled()) {
            log.trace("Game ends");
        }
    }

}
