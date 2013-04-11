import characters.*;
import map.Map;
import ui.GameUI;
import utility.*;

public final class GameEngine {
	private static Player player = null;
	
	private GameEngine() { }
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
		player = new Player("Nikita", new Position(1, 1));
		Map.moveGameCharacter(player, new Position(1, 1));
	}
	
	private static boolean handleInput() {
		char input = GameUI.getInputChar();		
		if (KeyDefinitions.isExitChar(input))
			return false;
		if (KeyDefinitions.isDirectionKey(input))
			player.move(DirectionProcessor.getDirectionFromChar(input));
		return true;
	}
	
	private static void exit() {
		GameUI.showMessage("You're leaving the game.");
		GameUI.close();
		System.exit(0);
	}
	
	// This method is a subject of constant changes.
	
	public static void play() {
		try {
		init();
		GameUI.showMessage("Prepare to play!");
		player.move(Direction.NORTH);
		player.performAction();
		GameUI.drawMap(Map.toStringArray());
		while (handleInput()) {
		if(player.hasAction())
			player.performAction();
		GameUI.drawMap(Map.toStringArray());
		}
		exit();
		} catch(Exception e) {
			GameUI.close();
			e.printStackTrace();
		}
 	}

}