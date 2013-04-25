package game.map;

import game.characters.GameCharacter;
import game.utility.*;

public final class LineOfSight {
	private final GameCharacter watcher;
	private boolean[][] seen;
	
	public LineOfSight(GameCharacter c, int size) {
		seen = new boolean[size * 2][size * 2];
		watcher = c;
	}
	
	private void updateLOS() {
		for (Direction d : Direction.values() )
			markSeen(new Position( seen.length / 2, seen.length / 2), d);
	}
	
	private void markSeen(Position pos, Direction there) {
		Position heroPos = watcher.getPosition();
		while ( insideSeenArray(pos) ) {
			if ( !Map.insideMap(heroPos) || !Map.isCellTransparent(heroPos) )
				break;
			seen[pos.x][pos.y] = true;
			pos = DirectionProcessor.applyDirectionToPosition(pos, there);
			heroPos = DirectionProcessor.applyDirectionToPosition(heroPos, there);
		}
			
	}
	
	public String toString() {
		
		updateLOS();
		
		char[][] map = Map.getVisibleChars(watcher.getPosition(), seen.length, seen.length);
		
		StringBuffer result = new StringBuffer();
		
		for (int y = map[0].length - 1; y >= 0; y--) {
			for (int x = 0; x < map.length; x++) {
				if (seen[x][y])
					result.append(map[x][y]);
				else
					result.append(" ");
			}
			result.append('\n');
		}
		
		return result.toString();
	}
	
	private boolean insideSeenArray(Position pos) {
		return pos.x < seen.length && pos.x >= 0 && pos.y < seen[0].length && pos.y >= 0;
	}
	
}
