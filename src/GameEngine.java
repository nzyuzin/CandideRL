import characters.*;
import map.Map;
import ui.GameUI;
import utility.*;
import java.util.ArrayList;

public final class GameEngine {
	private static Player player = null;
	private static ArrayList<NPC> npcs = null;
	static NPC  goblin = null;
	
	private GameEngine() { }
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
		npcs = new ArrayList<NPC>();
		player = new Player("Nikita", new Position(1, 1));
		goblin = new NPC("goblin", new Position(15, 15));
		npcs.add(goblin);
		Map.putGameCharacter(goblin, new Position(15, 15));
		Map.putGameCharacter(player, new Position(1, 1));
	}
	
	private static void handleInput() {
		char input = GameUI.getInputChar();		
		if (KeyDefinitions.isExitChar(input))
			exit();
		if (KeyDefinitions.isDirectionKey(input))
			player.move(DirectionProcessor.getDirectionFromChar(input));
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
		while (goblin.currentHP > 0) {
			handleInput();
			processActions();
			GameUI.drawMap(Map.toStringArray());
		}
		GameUI.showMessage("Goblin is killed!");
		Map.removeGameCharacter(goblin);
		player.breakActionQueue();
		while(player.currentHP > 0) {
			handleInput();
			processActions();
			GameUI.drawMap(Map.toStringArray());
		}
		exit();
		} catch(Exception e) {
			GameUI.close();
			e.printStackTrace();
		}
 	}

}