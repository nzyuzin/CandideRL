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
import com.github.nzyuzin.candiderl.game.GameState;
import com.github.nzyuzin.candiderl.game.map.generator.SquidDungeonGenerator;
import com.github.nzyuzin.candiderl.game.utility.KeyDefinitions;
import com.github.nzyuzin.candiderl.ui.GameUi;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Menu {

    private static final Logger log = LoggerFactory.getLogger(Menu.class);

    private static final String RETURN_TO_MAIN_MENU_OPTION = "Return to main menu";

    private enum StartMenuOption {
        NEW_GAME,
        LOAD_GAME,
        QUIT;

        @Override
        public String toString() {
            if (this == NEW_GAME) return "Start a new game";
            else if (this == LOAD_GAME) return "Load game";
            else return "Quit";
        }
    }

    private static final List<StartMenuOption> startScreenOptions =
            Lists.newArrayList(StartMenuOption.NEW_GAME, StartMenuOption.LOAD_GAME, StartMenuOption.QUIT);

    private final GameUi gameUi;

    public Menu(GameUi gameUi) {
        this.gameUi = gameUi;
    }

    public void start() {
        gameUi.init();
        showMainMenu();
    }

    private void showMainMenu() {
        while (true) {
            final StartMenuOption chosenOption = pollOptions(startScreenOptions);
            if (chosenOption == StartMenuOption.NEW_GAME) {
                final String playerName = askPlayerName();
                startNewGame(playerName);
            } else if (chosenOption == StartMenuOption.LOAD_GAME) {
                final String savefileName = askGameToLoad();
                if (!savefileName.equals(RETURN_TO_MAIN_MENU_OPTION)) {
                    final GameState gameState = loadGameState(savefileName);
                    continueGame(gameState);
                } // else continue
            } else {
                quit();
                break;
            }
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

    private String askGameToLoad() {
        final File currentDir = new File(".");
        File[] saveFiles = currentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".save");
            }
        });
        if (saveFiles == null) {
            return pollOptions(Lists.newArrayList(RETURN_TO_MAIN_MENU_OPTION));
        } else {
            List<String> saveFileNames =
                    Arrays.stream(saveFiles).map(File::getName).collect(Collectors.toList());
            saveFileNames.add(RETURN_TO_MAIN_MENU_OPTION);
            return pollOptions(saveFileNames);
        }
    }

    private void startNewGame(final String playerName) {
        try {
            GameEngine engine = new GameEngine(gameUi, new SquidDungeonGenerator(), playerName);
            engine.startGame();
        } catch (Exception ex) {
            log.error("Error during the game", ex);
            throw ex;
        }
    }

    private void continueGame(GameState gameState) {
        try {
            GameEngine engine = new GameEngine(gameUi, gameState);
            engine.startGame();
        } catch (Exception ex) {
            log.error("Error during the game", ex);
            throw ex;
        }
    }

    private GameState loadGameState(String filename) {
        GameState gameState;
        try {
            final FileInputStream fileInputStream = new FileInputStream(filename);
            final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            gameState = (GameState) objectInputStream.readObject();
            objectInputStream.close();
            return gameState;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void quit() {
        try {
            gameUi.close();
        } catch (Exception ex) {
            throw new RuntimeException("Exception during attempt to close the game", ex);
        }
    }

    private <T> T pollOptions(List<T> options) {
        gameUi.displayMenu(options);
        char input = gameUi.getInputChar();
        Optional<T> chosenOption = getOption(input, options);
        while (!chosenOption.isPresent()) {
            input = gameUi.getInputChar();
            chosenOption = getOption(input, options);
        }
        return chosenOption.get();
    }

    private <T> Optional<T> getOption(final char input, final List<T> options) {
        final int index = input - ((int) 'a');
        if (index < 0 || index >= options.size()) {
            return Optional.empty();
        } else {
            return Optional.of(options.get(index));
        }
    }
}
