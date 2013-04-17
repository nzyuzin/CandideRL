package game;
import game.ai.ArtificialIntelligence;
import game.characters.*;
import game.map.Map;
import game.ui.GameUI;
import game.utility.*;

import java.util.ArrayList;

// Should know about all classes without restrictions, but no class should know about existence of GameEngine.

public final class GameEngine {
	private static Player player = null;
	private static ArrayList<NPC> npcs = null;
	private static int currentTurn;
	private static ArtificialIntelligence artificialIntelligence = null;
	
	private GameEngine() { }
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
		npcs = new ArrayList<NPC>();
		player = new Player("DWARF", new Position(1, 1));
		npcs.add(new NPC("troll", 't', new Position(16,16)));
		artificialIntelligence = new ArtificialIntelligence(player);
		npcs.add(new NPC("goblin", 'g', new Position(15, 15)));
		Map.putGameCharacter(player, new Position(1, 1));
		for ( NPC mob : npcs ) 
			Map.putGameCharacter(mob, mob.getPosition());
		currentTurn = 0;
	}
	
	private static void processActions() {
		player.performAction();
		for (NPC npc : npcs) {
			if ( npc.canPerformAction() )
				npc.performAction();
			else npc.removeCurrentAction();
		}
	}
	
	private static void advanceTime() {
		for (NPC mob : npcs.toArray(new NPC[npcs.size()])) {
			if (mob.isDead())
				npcs.remove(mob);
			else artificialIntelligence.chooseAction(mob);
		}
		currentTurn++;
		processActions();
		drawMap();
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
	
	private static void drawMap() {
		GameUI.drawMap(Map.toStringArray());
	}
	
	// This method is a subject of constant changes.
	
	public static void play() {
		try {
		init();
		GameUI.showMessage("Prepare to play!");
		player.move(Direction.NORTH);
		player.performAction();
		drawMap();
		while ( !npcs.isEmpty() && !player.isDead() ) {
			handleInput();
			if ( player.canPerformAction() )
				advanceTime();
			else player.removeCurrentAction();
		}
		if ( npcs.isEmpty() ) GameUI.showMessage("All mobs are dead!");
		if ( player.isDead()) GameUI.showMessage("You're dead! Congratulations.");
		exit();
		} catch(Exception e) {
			GameUI.close();
			e.printStackTrace();
		}
 	}

}