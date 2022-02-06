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

import com.github.nzyuzin.candiderl.game.characters.NpcFactory;
import com.github.nzyuzin.candiderl.game.characters.Player;
import com.github.nzyuzin.candiderl.game.characters.actions.ActionFactory;
import com.github.nzyuzin.candiderl.game.characters.actions.WieldItemAction;
import com.github.nzyuzin.candiderl.game.fov.FovFactory;
import com.github.nzyuzin.candiderl.game.items.Weapon;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.map.generator.MapGenerator;
import com.github.nzyuzin.candiderl.ui.GameUi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public final class GameEngine {

    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    private final GameUi gameUi;
    private final KeyProcessor keyProcessor;
    private final ActionProcessor actionProcessor;

    private final GameState gameState;

    public GameEngine(GameUi gameUi, MapGenerator mapGenerator, String playerName) {
        this.gameUi = gameUi;
        final ActionFactory actionFactory = new ActionFactory();
        final NpcFactory npcFactory = new NpcFactory(new Random());
        final MapFactory mapFactory =
                new MapFactory(mapGenerator, npcFactory);
        final FovFactory fovFactory = new FovFactory();
        final GameFactories gameFactories = new GameFactories(actionFactory, fovFactory, mapFactory, npcFactory);

        this.gameState = new GameState(gameFactories);
        final Player player = new Player(this.gameState, playerName);
        this.gameState.setPlayer(player);

        actionFactory.setGameInformation(gameState);

        final Map map = mapFactory.build(gameState);
        map.putGameCharacter(player, map.getRandomFreePosition());
        gameState.addMap(map);

        this.keyProcessor = new KeyProcessor(gameUi, gameState);
        this.actionProcessor = new ActionProcessor(gameState);

        final Weapon broadsword = new Weapon("broadsword", "A regular sword", Weapon.Type.TWO_HANDED, 10, 1, 1);
        player.addItem(broadsword);
        new WieldItemAction(player, broadsword, 0).execute();
    }

    private void applyMapEffects() {
        gameState.getPlayer().getMap().applyEffects();
    }

    private void advanceTime() {
        if (log.isTraceEnabled()) {
            log.trace("advanceTime begin currentTurn = {}", gameState.getCurrentTime());
        }
        actionProcessor.scheduleActions();
        actionProcessor.processActions();
        actionProcessor.processEvents();
        applyMapEffects();
        gameUi.drawGame(gameState);
        if (log.isTraceEnabled()) {
            log.trace("advanceTime end");
        }
    }

    public void startGame() {
        if (log.isDebugEnabled()) {
            log.debug("Game starts");
        }
        gameUi.showAnnouncement("Greetings " + gameState.getPlayer().getName() + ", your journey awaits!");
        gameUi.drawGame(gameState);
        try {
            while (true) {
                keyProcessor.handleInput();
                if (gameState.getPlayer().hasAction()) {
                    advanceTime();
                } else {
                    final List<String> messages = gameState.getPlayer().pollMessages();
                    if (messages.size() > 0) {
                        for (final String message : messages) {
                            gameState.addMessage(message);
                        }
                        gameUi.drawMessages(gameState);
                    }
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
