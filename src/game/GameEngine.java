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
	private static MessageLog messageLog = null;
	private static int currentTurn;
	private static ArtificialIntelligence artificialIntelligence = null;
	
	private GameEngine() { }
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
		npcs = new ArrayList<NPC>();
		messageLog = new MessageLog(500);
		player = new Player("DWARF", new Position(1, 1));
		npcs.add(new NPC("troll", "A furious beast with sharp claws.", 't', new Position(11,1)));
		artificialIntelligence = new ArtificialIntelligence(player);
		npcs.add(new NPC("goblin", "A regular goblin.", 'g', new Position(10, 1)));
		Map.putGameCharacter(player, new Position(1, 1));
		for ( NPC mob : npcs ) 
			Map.putGameCharacter(mob, mob.getPosition());
		currentTurn = 0;
	}
	
	public static void showMessage(String msg) {
		messageLog.add(msg);
		GameUI.showMessage(msg);
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
		showStats();
	}
	
	private static void handleInput() {
		char input = GameUI.getInputChar();
		if (KeyDefinitions.isExitChar(input))
			exit();
		if (KeyDefinitions.isDirectionKey(input))
			artificialIntelligence.chooseActionInDirection(player, DirectionProcessor.getDirectionFromChar(input));
		if (input == 's')
			advanceTime();
	}
	
	private static void exit() {
		GameUI.showAnnouncement("You're leaving the game.");
		GameUI.exit();
		System.exit(0);
	}
	
	private static void drawMap() {
		GameUI.drawMap(Map.toOneString());
//		GameUI.drawMap(game.ai.PathFinder.costToString());
	}
	
	private static void showStats() {
		GameUI.showStats(player.getStats() + "Current turn: " + currentTurn + "\n");
	}
	
	// This method is a subject of constant changes.
	
	public static void play() {
		try {
		init();
		GameUI.showMessage("Prepare to play!");
		player.move(Direction.NORTH);
		player.performAction();
//		drawMap();
		showStats();
		while ( !npcs.isEmpty() && !player.isDead() ) {
			handleInput();
			if (player.hasAction())
				advanceTime();
			drawMap();
		}
		if ( npcs.isEmpty() ) GameUI.showAnnouncement("All mobs are dead!");
		if ( player.isDead()) GameUI.showAnnouncement("You're dead! Congratulations.");
		exit();
		} catch(Exception e) {
			GameUI.exit();
			e.printStackTrace();
		}
 	}

}