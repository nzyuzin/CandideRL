import map.Map;
import ui.GameUI;
import utility.KeyDefinitions;
import utility.DirectionProcessor;
import creatures.*;

public final class GameEngine {
	private static Hero hero = null;
	
	private GameEngine() { }
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
	}
	
	private static void handleInput() {
		char input = GameUI.getInputChar();
		int[] coordinates = null;
		
		while (!KeyDefinitions.isExitChar(input) ) {
			
			if ( KeyDefinitions.isDirectionKey(input) ) {
				
				coordinates = DirectionProcessor.applyDirectionToCoordinats( hero.posX, hero. posY, 
						DirectionProcessor.getDirectionFromChar(input) );
				
				if ( Map.isCellPassable(coordinates[0], coordinates[1]) ) {
					hero.move(coordinates[0], coordinates[1]);
					Map.moveCreature(hero, coordinates[0], coordinates[1]);
				}
				
				else if ( Map.creatureHere(coordinates[0], coordinates[1]) )
					hero.hit(Map.getCreature(coordinates[0], coordinates[1]));
			}
			
			input = GameUI.getInputChar();
		}
		exit();
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
		GameUI.showMessage("Prepare to play! Press space to start.");

		GameUI.close();
		} catch(Exception e) {
			GameUI.close();
			e.printStackTrace();
		}
 	}

}
