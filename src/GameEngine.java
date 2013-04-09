import map.Map;
import movement.Direction;
import ui.GameUI;
import creatures.*;

public class GameEngine {
	private static Hero hero = null;
	private final static String HERO_NAME = "NONAME";
	
	private static void init() {
		GameUI.init();
		Map.init(GameUI.getMapWidth(), GameUI.getMapHeight());
		hero = Map.spawnHero(HERO_NAME);
	}
	
	private static void handleInput() {
		char input = GameUI.getInputChar();
		
		switch (input) {
		
		case 'j':
			if(hero.canMove(Direction.SOUTH))
				hero.move(Direction.SOUTH);
			break;
		case 'k':
			if(hero.canMove(Direction.NORTH))
				hero.move(Direction.NORTH);
			break;
		case 'h':
			if(hero.canMove(Direction.WEST))
				hero.move(Direction.WEST);
			break;
		case 'l':
			if(hero.canMove(Direction.EAST))
				hero.move(Direction.EAST);
			break;
		case 'y':
			if(hero.canMove(Direction.NORTHWEST))
				hero.move(Direction.NORTHWEST);
			break;
		case 'u':
			if(hero.canMove(Direction.NORTHEAST))
				hero.move(Direction.NORTHEAST);
			break;
		case 'n':
			if(hero.canMove(Direction.SOUTHWEST))
				hero.move(Direction.SOUTHWEST);
			break;
		case 'm':
			if(hero.canMove(Direction.SOUTHEAST))
				hero.move(Direction.SOUTHEAST);
			break;
		case 'q':
			quit();
		default:
			handleInput();
			return;
		
		}
	}
	
	private static void quit() {
		GameUI.showMessage("You're leaving the game.");
		GameUI.close();
		System.exit(0);
	}
	
	public static void play() {
		try {
		init();
		GameUI.showMessage("Prepare to play! Press any key to start.");
		NPC goblin = Map.spawnMonster("goblin");
		while(goblin.currentHP > 0) {
			GameUI.drawMap();
			handleInput();
		}
		
		GameUI.showMessage("Congratulations, you have beaten the goblin and won the game!");
		GameUI.close();
		} catch(Exception e) {
			GameUI.close();
			e.printStackTrace();
		}
 	}

}
