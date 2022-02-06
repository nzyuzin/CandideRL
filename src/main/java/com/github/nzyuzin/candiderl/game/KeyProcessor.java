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

import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.items.Item;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.Door;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.map.cells.Stairs;
import com.github.nzyuzin.candiderl.game.utility.Direction;
import com.github.nzyuzin.candiderl.game.utility.KeyDefinitions;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.ui.GameUi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KeyProcessor {

    private static final Logger log = LoggerFactory.getLogger(KeyProcessor.class);

    private final GameUi ui;
    private final GameState gameState;

    public KeyProcessor(GameUi ui, GameState gameInformation) {
        this.ui = ui;
        this.gameState = gameInformation;
    }

    public void handleInput() throws GameClosedException {
        final char input = getInput();
        if (KeyDefinitions.STATUS_KEY == input) {
            ui.showStatus(gameState);
            getInput(); // Skip one char to close the status screen
            drawGameScreen();
        } if (KeyDefinitions.INVENTORY_KEY == input ) {
            showInventory();
        } else if (KeyDefinitions.VIEW_MODE_KEY == input) {
            enterViewMode();
        } else if (KeyDefinitions.isDirectionKey(input) && !gameState.getPlayer().isDead()) {
            processDirectionKey(input);
        } else if (KeyDefinitions.OPEN_DOOR_KEY == input) {
            openCloseDoor(true);
        } else if (KeyDefinitions.CLOSE_DOOR_KEY == input) {
            openCloseDoor(false);
        } else if (KeyDefinitions.PICKUP_ITEM_KEY == input) {
            pickupItem();
        } else if (KeyDefinitions.STAIRS_UPWARDS_KEY == input || KeyDefinitions.STAIRS_DOWNWARDS_KEY == input) {
            processStairs(input);
        } else if (KeyDefinitions.isSkipTurnChar(input)) {
            gameState.getPlayer().skipTurn();
        } else if (KeyDefinitions.isExitChar(input)) {
            throw new GameClosedException();
        }
    }

    private char getInput() {
        final char input = ui.getInputChar();
        if (log.isTraceEnabled()) {
            log.trace("handleInput input = {}, {}", input, (int) input);
        }
        return input;
    }

    private void processDirectionKey(final char input) {
        final Direction direction = KeyDefinitions.getDirectionFromKey(input);
        final Player player = gameState.getPlayer();
        final Position position = player.getPosition().apply(direction);
        final MapCell mapCell = player.getMap().getCell(position);
        if (mapCell.isPassable()) {
            if (!mapCell.getGameCharacter().isPresent()) {
                player.move(position);
            } else {
                player.hit(position);
            }
        } else {
            if (mapCell instanceof Door && ((Door) mapCell).isClosed()) {
                player.openDoor(position);
            }
        }
    }

    private void enterViewMode() {
        Position lastPosition = gameState.getPlayer().getPosition();
        final Map map = gameState.getPlayer().getMap();
        ui.drawMapView(gameState, map, lastPosition);
        char newInput = getInput();
        while (newInput != KeyDefinitions.ESCAPE_KEY) {
            if (KeyDefinitions.isDirectionKey(newInput)) {
                final Direction direction = KeyDefinitions.getDirectionFromKey(newInput);
                final Position newPosition = lastPosition.apply(direction);
                if (map.isInside(newPosition)) {
                    lastPosition = newPosition;
                    ui.drawMapView(gameState, map, lastPosition);
                }
            } else if (KeyDefinitions.EXAMINE_KEY == newInput) {
                final MapCell cell = map.getCell(lastPosition);
                if (cell.getGameCharacter().isPresent()) {
                    ui.drawExamineScreen(cell.getGameCharacter().get());
                } else if (!cell.getItems().isEmpty()) {
                    ui.drawExamineScreen(cell.getItems().get(0));
                } else {
                    ui.drawExamineScreen(cell);
                }
                getInput();
                ui.drawMapView(gameState, map, lastPosition);
            }
            newInput = getInput();
        }
        drawGameScreen();
    }

    private void openCloseDoor(boolean isOpen) {
        final Player player = gameState.getPlayer();
        reportMessage((isOpen ? "Open" : "Close") + " in which direction?");
        char input = getInput();
        while (!KeyDefinitions.isDirectionKey(input)) {
            input = getInput();
        }
        final Position doorPosition = player.getPosition().apply(KeyDefinitions.getDirectionFromKey(input));
        final MapCell doorCell = player.getMap().getCell(doorPosition);
        if (!(doorCell instanceof Door)) {
            reportMessage("There is no door here!");
        } else {
            final Door door = (Door) doorCell;
            if (isOpen) {
                if (door.isOpen()) {
                    reportMessage("This door is already opened!");
                } else {
                    player.openDoor(doorPosition);
                }
            } else {
                if (door.isClosed()) {
                    reportMessage("This door is already closed!");
                } else {
                    player.closeDoor(doorPosition);
                }
            }
        }
    }

    private void processStairs(final char direction) {
        final Stairs.Type type = direction == KeyDefinitions.STAIRS_DOWNWARDS_KEY ? Stairs.Type.DOWN : Stairs.Type.UP;
        gameState.getPlayer().traverseStairs(type);
    }

    private void pickupItem() {
        final Player player = gameState.getPlayer();
        final MapCell playerCell = player.getMapCell();
        if (!playerCell.getItems().isEmpty()) {
            final Item item = playerCell.getItems().get(0);
            player.pickupItem(item);
        } else {
            reportMessage("There is nothing to pick up");
        }
    }

    private void showInventory() {
        ui.showInventory(gameState);
        char newInput = getInput();
        final List<Item> playerItems = gameState.getPlayer().getItems();
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
        ui.showItem(item);
        char input = getInput();
        while(input != KeyDefinitions.ESCAPE_KEY) {
            if (input == KeyDefinitions.DROP_ITEM_KEY) {
                gameState.getPlayer().dropItem(item);
                return true;
            } else if (input == KeyDefinitions.WIELD_ITEM_KEY) {
                gameState.getPlayer().wieldItem(item);
                return true;
            }

            input = getInput();
        }
        ui.showInventory(gameState);
        return false;
    }

    private void reportMessage(final String msg) {
        gameState.addMessage(msg);
        drawGameScreen();
    }

    private void drawGameScreen() {
        ui.drawGame(gameState);
    }
}
