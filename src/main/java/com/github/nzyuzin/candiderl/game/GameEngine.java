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
import com.github.nzyuzin.candiderl.game.characters.ItemSlot;
import com.github.nzyuzin.candiderl.game.characters.Npc;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.items.Weapon;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.KeyDefinitions;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import com.github.nzyuzin.candiderl.ui.GameUi;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class GameEngine {

    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    private final List<Npc> npcs;
    private final List<Event> events;
    private final GameUi gameUi;
    private final NpcController npcController;

    private PositionOnMap playerPosition;

    private final GameInformation gameInformation;

    public static GameEngine getGameEngine(MapFactory mapFactory, GameUi gameUi) {
        return new GameEngine(gameUi, mapFactory.getMap());
    }

    private GameEngine(GameUi gameUi, Map map) {
        this.gameUi = gameUi;
        npcs = Lists.newArrayList();
        events = Lists.newArrayList();
        final Player player = Player.getInstance();
        int npcOperationalRange = 20; // arbitrary for now
        npcController = new NpcController(player, npcOperationalRange);
        if (GameConfig.SPAWN_MOBS) {
            npcs.add(new Npc(
                            "Troll",
                            "A furious beast with sharp claws.",
                            ColoredChar.getColoredChar('t', ColoredChar.RED))
            );
            for (Npc mob : npcs)
                map.putGameCharacter(mob, map.getRandomFreePosition());
        }
        map.putGameCharacter(player, map.getRandomFreePosition());
        final Weapon broadsword = new Weapon("broadsword", "A regular sword", Weapon.Type.Sword, 10, 1, 1);
        player.addItem(broadsword);
        player.setItem(player.getItemSlots().get(0), broadsword);
        playerPosition = player.getPositionOnMap();
        gameInformation = new GameInformation(player);
    }

    private void processActions() {
        events.addAll(gameInformation.getPlayer().performAction());
        for (Iterator<Npc> iterator = npcs.iterator(); iterator.hasNext(); ) {
            Npc npc = iterator.next();
            if (npc.isDead()) {
                iterator.remove();
                continue;
            } else npcController.chooseAction(npc);
            if (npc.canPerformAction()) {
                events.addAll(npc.performAction());
            } else {
                npc.removeCurrentAction();
            }
        }
    }

    private void processEvents() {
        final ListIterator<Event> eventsIterator = events.listIterator();
        while (eventsIterator.hasNext()) {
            final Event e = eventsIterator.next();
            e.occur();
            if (!Strings.isNullOrEmpty(e.getTextualDescription())) {
                gameInformation.addMessage(e.getTextualDescription());
            }
            eventsIterator.remove();
        }
    }

    private void applyMapEffects() {
        playerPosition.getMap().applyEffects();
    }

    private void advanceTime() {
        if (log.isTraceEnabled()) {
            log.trace("advanceTime begin currentTurn = {}", gameInformation.getCurrentTurn());
        }
        processActions();
        processEvents();
        applyMapEffects();
        if (gameInformation.getPlayer().getPositionOnMap() != null) {
            playerPosition = gameInformation.getPlayer().getPositionOnMap();
        }
        drawGameScreen();
        gameInformation.incrementTurn();
        if (log.isTraceEnabled()) {
            log.trace("advanceTime end");
        }
    }

    private char getInput() {
        final char input = gameUi.getInputChar();
        if (log.isTraceEnabled()) {
            log.trace("handleInput input = {}, {}", input, (int) input);
        }
        return input;
    }

    private void handleInput() throws GameClosedException {
        final char input = getInput();
        if (KeyDefinitions.STATUS_KEY == input) {
            gameUi.showStatus(gameInformation);
            getInput(); // Skip one char to close the status screen
            drawGameScreen();
        } if (KeyDefinitions.INVENTORY_KEY == input ) {
            showInventory();
        } else if (KeyDefinitions.isDirectionKey(input) && !gameInformation.getPlayer().isDead()) {
            npcController.chooseActionInDirection(gameInformation.getPlayer(), KeyDefinitions.getDirectionFromKey(input));
        } else if (KeyDefinitions.PICKUP_ITEM_KEY == input) {
            pickupItem();
        } else if (KeyDefinitions.isSkipTurnChar(input)) {
            advanceTime();
        } else if (KeyDefinitions.isExitChar(input)) {
            throw new GameClosedException();
        }
    }

    private void pickupItem() {
        final Player player = gameInformation.getPlayer();
        final MapCell playerCell = player.getMapCell();
        if (!playerCell.getItems().isEmpty()) {
            final Item item = playerCell.getItems().get(0);
            player.pickupItem(item);
            gameInformation.addMessage("You pick up " + item);
        }
        drawGameScreen();
    }

    private void showInventory() {
        gameUi.showInventory(gameInformation);
        char newInput = getInput();
        final List<Item> playerItems = gameInformation.getPlayer().getItems();
        while (newInput != KeyDefinitions.ESCAPE_KEY) {
            final int inventoryItem = newInput - 'a';
            if (inventoryItem >= 0 && inventoryItem < 52 && inventoryItem < playerItems.size()) {
                if (showItem(playerItems.get(inventoryItem))) {
                    break;
                }
            }
            newInput = getInput();
        }
        drawGameScreen();
    }

    /**
     * Displays chosen item
     * @param item
     * @return true if escape to game screen should be performed, false if returning to previous screen
     */
    private boolean showItem(final Item item) {
        gameUi.showItem(item);
        char input = getInput();
        while(input != KeyDefinitions.ESCAPE_KEY) {
            if (input == KeyDefinitions.DROP_ITEM_KEY) {
                gameInformation.getPlayer().dropItem(item);
                gameInformation.addMessage("You drop " + item);
                return true;
            } else if (input == KeyDefinitions.WIELD_ITEM_KEY) {
                wieldItem(item);
                return true;
            }

            input = getInput();
        }
        gameUi.showInventory(gameInformation);
        return false;
    }

    private void wieldItem(final Item item) {
        Preconditions.checkArgument(gameInformation.getPlayer().getItems().contains(item), "Item should be in the inventory!");
        for (final ItemSlot itemSlot : gameInformation.getPlayer().getItemSlots()) {
            if (itemSlot.getType() == ItemSlot.Type.HAND && !itemSlot.getItem().isPresent()) {
                itemSlot.setItem(item);
                gameInformation.addMessage("You wield " + item);
                drawGameScreen();
                return;
            }
        }
        gameInformation.addMessage("You do not have free hands to wield that!");
        drawGameScreen();
    }

    private void drawGameScreen() {
        long initTime = 0;
        if (log.isTraceEnabled()) {
            log.trace("drawUi start");
            initTime = System.currentTimeMillis();
        }
        gameUi.drawGame(gameInformation);
        if (log.isTraceEnabled()) {
            log.trace(String.format("drawUi end :: time=%d", System.currentTimeMillis() - initTime));
        }
    }

    public void startGame() {
        if (log.isDebugEnabled()) {
            log.debug("Game starts");
        }
        gameUi.showAnnouncement("Journey awaits!");
        drawGameScreen();
        try {
            while (true) {
                handleInput();
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
