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

package com.github.nzyuzin.candiderl.game;

import com.github.nzyuzin.candiderl.game.ai.NpcController;
import com.github.nzyuzin.candiderl.game.characters.NPC;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.ui.GameUI;
import com.github.nzyuzin.candiderl.game.ui.swing.SwingGameUI;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.KeyDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GameEngine implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    public static void main(String args[]) {
        try (GameEngine engine = getGameEngine()) {
            engine.play();
        } catch (Exception ex) {
            log.error("Error during game", ex);
            throw ex;
        }
    }

    private Player player;
    private List<NPC> npcs;
    private int currentTurn = 0;
    private GameUI ui;
    private NpcController npcController;

    public static GameEngine getGameEngine() {
        MapFactory mapFactory = MapFactory.getInstance(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT);
        return new GameEngine(SwingGameUI.getUI(), mapFactory.getMap());
    }

    private GameEngine(GameUI ui, Map map) {
        if (log.isTraceEnabled()) {
            log.trace("mapFactory done");
        }
        this.ui = ui;
        npcs = new ArrayList<>();
        player = Player.getInstance();
        int npcOperationalRange = (this.ui.getMapWidth() + this.ui.getMapHeight()) / 2;
        npcController = new NpcController(player, npcOperationalRange);
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
                map.putGameCharacter(mob, map.getRandomFreePosition());
        }
        map.putGameCharacter(player, map.getRandomFreePosition());
        currentTurn = 0;
    }

    private void processActions() {
        player.performAction();
        for (Iterator<NPC> iterator = npcs.iterator(); iterator.hasNext(); ) {
            NPC npc = iterator.next();
            if (npc.isDead()) {
                iterator.remove();
                continue;
            } else npcController.chooseAction(npc);
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
        char input = ui.getInputChar();
        if (log.isDebugEnabled()) {
            log.debug(String.format("handleInput input = %s", input));
        }
        if (KeyDefinitions.isDirectionKey(input)) {
            npcController.chooseActionInDirection(player, KeyDefinitions.getDirectionFromKey(input));
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
        ui.showAnnouncement("You're leaving the game.");
        try {
            ui.close();
        } catch (Exception ex) {
            throw new RuntimeException("Exception during attempt to close the game", ex);
        }
    }

    private void drawMap() {
        long initTime = 0;
        if (log.isTraceEnabled()) {
            log.trace("drawMap start");
            initTime = System.currentTimeMillis();
        }
        if (!player.isDead()) {
            ui.drawMap(player.getVisibleMap(ui.getMapWidth(), ui.getMapHeight()));
        }
        if (log.isTraceEnabled()) {
            log.trace(String.format("drawMap end :: time=%d", System.currentTimeMillis() - initTime));
        }
    }

    private void showStats() {
        ui.showStats(String.format("%s%nCurrent turn: %d%n", player.getStats(), currentTurn));
    }

    public void play() {
        if (log.isInfoEnabled()) {
            log.info("Game starts");
        }
        ui.showMessage("Prepare to play!");
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
        if (npcs.isEmpty()) ui.showAnnouncement("All mobs are dead!");
        if (player.isDead()) ui.showAnnouncement("You're dead!");
        if (log.isInfoEnabled()) {
            log.info("Game ends");
        }
    }

}
