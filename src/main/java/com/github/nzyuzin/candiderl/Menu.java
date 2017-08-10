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

package com.github.nzyuzin.candiderl;

import com.github.nzyuzin.candiderl.game.GameEngine;
import com.github.nzyuzin.candiderl.game.characters.NpcFactory;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.map.generator.SquidDungeonGenerator;
import com.github.nzyuzin.candiderl.game.utility.KeyDefinitions;
import com.github.nzyuzin.candiderl.ui.GameUi;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Menu {

    private static final Logger log = LoggerFactory.getLogger(Menu.class);

    private enum StartMenuOption {
        START_GAME,
        QUIT;

        @Override
        public String toString() {
            if (this == START_GAME) return "Start game";
            else return "Quit";
        }
    }

    private static final List<StartMenuOption> startScreenOptions =
            Lists.newArrayList(StartMenuOption.START_GAME, StartMenuOption.QUIT);

    private final GameUi gameUi;

    public Menu(GameUi gameUi) {
        this.gameUi = gameUi;
    }

    public void start() {
        gameUi.init();
        gameUi.displayMenu(startScreenOptions);
        char input = gameUi.getInputChar();
        Optional<Object> chosenOption = getOption(input, startScreenOptions);
        while (!chosenOption.isPresent()) {
            input = gameUi.getInputChar();
            chosenOption = getOption(input, startScreenOptions);
        }
        if (chosenOption.get() == StartMenuOption.START_GAME) {
            final String playerName = askPlayerName();
            startGame(playerName);
        } else {
            quit();
        }
    }

    private String askPlayerName() {
        String name = "";
        final String enterNameQuery = "Enter name:";
        final int nameLength = 15;
        gameUi.drawInputForm(enterNameQuery, nameLength, name);
        char input = gameUi.getInputChar();
        while (name.isEmpty() || input != KeyDefinitions.LF_KEY) {
            if (Character.isAlphabetic(input) && name.length() < nameLength) {
                name += input;
            } else if (input == KeyDefinitions.BACKSPACE_KEY && name.length() > 0) {
                name = name.substring(0, name.length() - 1);
            }
            gameUi.drawInputForm(enterNameQuery, nameLength, name);
            input = gameUi.getInputChar();
        }
        return name;
    }

    private void startGame(final String playerName) {
        MapFactory mapFactory = new MapFactory(new SquidDungeonGenerator(new NpcFactory(new Random())));
        try {
            GameEngine engine = new GameEngine(gameUi, mapFactory, playerName);
            engine.startGame();
            quit();
        } catch (Exception ex) {
            log.error("Error during the game", ex);
            throw ex;
        }
    }

    private void quit() {
        try {
            gameUi.close();
        } catch (Exception ex) {
            throw new RuntimeException("Exception during attempt to close the game", ex);
        }
    }

    private Optional<Object> getOption(final char input, final List<? extends Object> options) {
        final int code = (int) input;
        final int index = code - ((int) 'a');
        if (index < 0 || index >= options.size()) {
            return Optional.empty();
        } else {
            return Optional.of(options.get(index));
        }
    }
}
