package game;
import game.ai.AI;
import game.characters.*;
import game.map.Map;
import game.ui.GameUI;
import game.utility.*;

import java.util.ArrayList;

// Should know about all classes without restrictions, but no class should know about existence of GameEngine.

public final class GameEngine {
	private static Player player = null;
	private static ArrayList<NPC> npcs = null;
	static NPC  goblin = null;
	private static int currentTurn;
	private static AI artificialIntellegence = null;
	
	private GameEngine() { }
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
		npcs = new ArrayList<NPC>();
		player = new Player("DWARF", new Position(47, 1));
		goblin = new NPC("goblin", new Position(15, 15));
		artificialIntellegence = new AI(player);
		npcs.add(goblin);
		Map.putGameCharacter(goblin, new Position(15, 15));
		Map.putGameCharacter(player, new Position(47, 1));
		currentTurn = 0;
	}
	
	private static void advanceTime() {
		for (NPC mob : npcs)
			artificialIntellegence.chooseAction(mob);
		currentTurn++;
		processActions();
		GameUI.drawMap(Map.toStringArray());
	}
	
	private static void handleInput() {
		char input = GameUI.getInputChar();		
		if (KeyDefinitions.isExitChar(input))
			exit();
		if (KeyDefinitions.isDirectionKey(input))
			player.move(DirectionProcessor.getDirectionFromChar(input));
		if (input == 's')
			advanceTime();
	}
	
	private static void exit() {
		GameUI.showMessage("You're leaving the game.");
		GameUI.close();
		System.exit(0);
	}
	
	private static void processActions() {
		player.performAction();
		for (NPC npc : npcs)
			npc.performAction();
	}
	
	// This method is a subject of constant changes.
	
	public static void play() {
		try {
		init();
		GameUI.showMessage("Prepare to play!");
		player.move(Direction.NORTH);
		player.performAction();
		GameUI.drawMap(Map.toStringArray());
		while ( !goblin.isDead() && !player.isDead() ) {
			handleInput();
			advanceTime();
		}
		if ( goblin.isDead()) GameUI.showMessage("Goblin is killed!");
		if ( player.isDead()) GameUI.showMessage("You're dead! Congratulations.");
		exit();
		} catch(Exception e) {
			GameUI.close();
			e.printStackTrace();
		}
 	}

}