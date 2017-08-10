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

package com.github.nzyuzin.candiderl.game;

import com.github.nzyuzin.candiderl.game.ai.NpcController;
import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.characters.Npc;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionResult;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.items.Weapon;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.ui.GameUi;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ListIterator;

public final class GameEngine {

    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    private final List<Event> events;
    private final GameUi gameUi;
    private final NpcController npcController;
    private final KeyProcessor keyProcessor;

    private final GameInformation gameInformation;

    public GameEngine(GameUi gameUi, MapFactory mapFactory, String playerName) {
        final Map map = mapFactory.build();
        this.gameUi = gameUi;
        events = Lists.newArrayList();
        final Player player = new Player(playerName);
        int npcOperationalRange = 20; // arbitrary for now
        map.putGameCharacter(player, map.getRandomFreePosition());
        final Weapon broadsword = new Weapon("broadsword", "A regular sword", Weapon.Type.TWO_HANDED, 10, 1, 1);
        player.addItem(broadsword);
        player.wieldItem(broadsword);
        npcController = new NpcController(player, npcOperationalRange);
        gameInformation = new GameInformation(player);
        this.keyProcessor = new KeyProcessor(gameUi, gameInformation, mapFactory);
        processActions();
        processEvents();
    }

    private void processActions() {
        final Player player = gameInformation.getPlayer();
        processAction(player);
        for (final GameCharacter character : player.getMap().getCharacters()) {
            if (!(character instanceof Npc)) {
                continue;
            }
            final Npc npc = (Npc) character;
            if (!npc.getPositionOnMap().getMap().equals(player.getMap())) {
                // Skip mob if it is not on the same map as player
                continue;
            }
            if (npc.isDead()) {
                throw new AssertionError("Dead mob found on turn " + gameInformation.getCurrentTurn() + " name: " + npc.getName());
            } else {
                npcController.chooseAction(npc);
            }
            if (npc.canPerformAction()) {
                processAction(npc);
            } else {
                npc.removeCurrentAction();
            }
        }
    }

    private void processAction(GameCharacter gameCharacter) {
        final ActionResult actionResult = gameCharacter.performAction();
        events.addAll(actionResult.getEvents());
        gameInformation.addMessage(actionResult.getMessage());
    }

    private void processEvents() {
        final ListIterator<Event> eventsIterator = events.listIterator();
        while (eventsIterator.hasNext()) {
            final Event e = eventsIterator.next();
            e.occur();
            gameInformation.addMessage(e.getTextualDescription());
            eventsIterator.remove();
        }
    }

    private void applyMapEffects() {
        gameInformation.getPlayer().getMap().applyEffects();
    }

    private void advanceTime() {
        if (log.isTraceEnabled()) {
            log.trace("advanceTime begin currentTurn = {}", gameInformation.getCurrentTurn());
        }
        processActions();
        processEvents();
        applyMapEffects();
        gameInformation.incrementTurn();
        gameUi.drawGame(gameInformation);
        if (log.isTraceEnabled()) {
            log.trace("advanceTime end");
        }
    }

    public void startGame() {
        if (log.isDebugEnabled()) {
            log.debug("Game starts");
        }
        gameUi.showAnnouncement("Greetings " + gameInformation.getPlayer().getName() + ", your journey awaits!");
        gameUi.drawGame(gameInformation);
        try {
            while (true) {
                keyProcessor.handleInput();
                if (gameInformation.getPlayer().canPerformAction()) {
                    advanceTime();
                }
            }
        } catch (GameClosedException gameClosedException) {
            gameUi.showAnnouncement("You are leaving the game.");
        } finally {
            GameConfig.write();
        }
        if (log.isDebugEnabled()) {
            log.debug("Game ends");
        }
    }
}
