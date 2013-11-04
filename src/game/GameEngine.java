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
import game.characters.*;
import game.map.Map;
import game.map.MapFactory;
import game.ui.GameUI;
import game.ui.swing.SwingGameUI;
import game.utility.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

import java.util.ArrayList;
import java.util.Random;

public final class GameEngine implements AutoCloseable {

    private static final Logger log = Logger.getRootLogger();

    private static final String LOG_CONVERSION_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%p] %C.%M:%L - %m%n";

	public static void main(String args[]) {
		try (GameEngine engine = getGameEngine()) {
            engine.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private Player player = null;
	private ArrayList<NPC> npcs = null;
	private MessageLog messageLog = null;
	private int currentTurn;
    private Map currentMap = null;
    private final GameUI UI;

    public static GameEngine getGameEngine() {
        return new GameEngine();
    }

	private GameEngine() {

        initLog();

        UI = SwingGameUI.getUI();
        MapFactory mapFactory = MapFactory.getInstance();
        mapFactory.setScreenSize(UI.getScreenWidth(), UI.getScreenHeight());
        currentMap = mapFactory.getMap();

        log.trace("mapFactory done");

        npcs = new ArrayList<>();

        // To be implemented
        messageLog = new MessageLog(500);

        player = Player.getInstance();

        //Some magic constants here
        ArtificialIntelligence.init(player, (UI.getMapWidth() + UI.getMapHeight()) / 2 + 100);

        Random rand = new Random();

//        npcs.add(new NPC("troll", "A furious beast with sharp claws.",new ColoredChar('t', ColoredChar.RED)));
//		npcs.add(new NPC("goblin", "A regular goblin.",new ColoredChar('g', ColoredChar.GREEN)));

        for (NPC mob : npcs)
            currentMap.putGameCharacter(mob, currentMap.getRandomFreeCell());
        currentMap.putGameCharacter(player, currentMap.getRandomFreeCell());

        currentTurn = 0;
    }

    private void initLog() {
        log.addAppender(new ConsoleAppender(new PatternLayout(LOG_CONVERSION_PATTERN), ConsoleAppender.SYSTEM_OUT));
        log.setLevel(Level.TRACE);
    }

	private void processActions() {
		player.performAction();
		for (NPC npc : npcs.toArray(new NPC[npcs.size()])) {
			if (npc.isDead()) {
				npcs.remove(npc);
				continue;
			}
			else ArtificialIntelligence.chooseAction(npc);
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
            log.trace("advanceTime end :: currentTurn = " + currentTurn);
        }
	}

	private void handleInput() throws Exception {
		char input = UI.getInputChar();
        if (log.isDebugEnabled() && input != '\n') {
            log.debug("handleInput input = " + input);
        }
		if (KeyDefinitions.isDirectionKey(input)) {
			ArtificialIntelligence.chooseActionInDirection(player, Direction.getDirection(input));
            return;
        }
		if (KeyDefinitions.isSkipTurnChar(input)) {
			advanceTime();
            return;
        }
        if (KeyDefinitions.isExitChar(input)) {
            close();
            return;
        }
	}

	public void close() throws Exception {
		UI.showAnnouncement("You're leaving the game.");
		UI.close();
        System.exit(0);
	}

	private void drawMap() {
        log.trace("drawMap start");
        long initTime = System.currentTimeMillis();
		if (!player.isDead()) {
			UI.drawMap(player.getVisibleMap());
		}
        long endTime = System.currentTimeMillis();
        log.trace("drawMap end :: time=" + (endTime - initTime));
	}

	private void showStats() {
		UI.showStats(player.getStats() + "Current turn: "
					+ currentTurn + "\n");
	}

    public void play() throws Exception {
        log.trace("Game starts");
        UI.showMessage("Prepare to play!");
        drawMap();
        showStats();
        while ( !player.isDead() ) {
            handleInput();
            if (player.hasAction())
                advanceTime();
        }
        if (npcs.isEmpty()) UI.showAnnouncement("All mobs are dead!");
        if (player.isDead()) UI.showAnnouncement("You're dead!");
        log.trace("Game ends");
     }

}
