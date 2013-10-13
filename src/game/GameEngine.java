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
import game.utility.*;

import java.util.ArrayList;

public final class GameEngine {

	public static void main(String args[]) {
		GameEngine engine = new GameEngine();
        engine.play();
	}

    private static final GameEngine INSTANCE = new GameEngine();

	private Player player = null;
	private ArrayList<NPC> npcs = null;
	private MessageLog messageLog = null;
	private int currentTurn;
    private Map currentMap = null;
    private final GameUI UI;

	private GameEngine() {
        UI = GameUI.getInstance();
        MapFactory.getInstance().setScreenSize(UI.getScreenWidth(), UI.getScreenHeight());
        currentMap = MapFactory.getInstance().getMap();
        npcs = new ArrayList<>();
        messageLog = new MessageLog(500);
        player = Player.getInstance();
        ArtificialIntelligence.init(player,
                (UI.getMapWidth() + UI.getMapHeight()) / 2 + 100);

        npcs.add(new NPC("troll", "A furious beast with sharp claws.",
                new ColoredChar('t', ColoredChar.RED)));
//		npcs.add(new NPC("goblin", "A regular goblin.",
//				new ColoredChar('g', ColoredChar.GREEN)));

        currentMap.putGameCharacter(player, Position.getPosition(43, 1));
        for ( NPC mob : npcs )
            currentMap.putGameCharacter(mob, Position.getPosition(1, 1));
        currentTurn = 0;
    }

    public static GameEngine getInstance() {
        return INSTANCE;
    }

	private void processActions() {
		player.performAction();
		for (NPC npc : npcs.toArray(new NPC[npcs.size()])) {
			if (npc.isDead()) {
				npcs.remove(npc);
				continue;
			}
			else ArtificialIntelligence.chooseAction(npc);
			if ( npc.canPerformAction() )
				npc.performAction();
			else npc.removeCurrentAction();
		}
	}

	private void advanceTime() {
		currentTurn++;
		processActions();
		drawMap();
		showStats();
	}

	private void handleInput() {
		char input = UI.getInputChar();
		if (KeyDefinitions.isExitChar(input))
			exit();
		if (KeyDefinitions.isDirectionKey(input))
			ArtificialIntelligence
			.chooseActionInDirection(player, Direction.getDirection(input));
		if (input == 's')
			advanceTime();
	}

	private void exit() {
		UI.showAnnouncement("You're leaving the game.");
		UI.exit();
		System.exit(0);
	}

	private void drawMap() {
		if (!player.isDead()) {
			UI.drawMap(player.getVisibleMap());
		}
	}

	private void showStats() {
		UI.showStats(player.getStats() + "Current turn: "
					+ currentTurn + "\n");
	}

	public void play() {
        Exception e = null;
        try {
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
            exit();
        } catch(Exception exc) {
            e = exc;
        } finally {
            UI.exit();
            if (e != null) {
                e.printStackTrace();
            }
        }
     }

}
