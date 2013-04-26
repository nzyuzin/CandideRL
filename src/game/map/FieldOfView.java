package game.map;

import game.characters.GameCharacter;
import game.utility.*;

public final class FieldOfView {
	private final GameCharacter watcher;
	private boolean[][] seen;
	
	public FieldOfView(GameCharacter c, int size) {
		seen = new boolean[Map.getWidth()][Map.getHeight()];
		watcher = c;
	}
	
	private void updateFOV() {
		for (Direction d : Direction.values() )
			markSeen(new Position( seen.length / 2, seen[0].length / 2), d);
	}
	
	private void markSeen(Position pos, Direction there) {
		Position mapPos = watcher.getPosition();
		while ( insideSeenArray(pos) && Map.insideMap(mapPos) ) {
//			if (Map.isCellTransparent(mapPos))
			 seen[pos.x][pos.y] = true;
			pos = DirectionProcessor.applyDirectionToPosition(pos, there);
			mapPos = DirectionProcessor.applyDirectionToPosition(mapPos, there);
		}
			
	}
	
	public String toString() {
		
		updateFOV();
		
		char[][] map = Map.getVisibleChars(watcher.getPosition());
		
		StringBuffer result = new StringBuffer();
		
		for (int y = map[0].length - 1; y >= 0; y--)
			for (int x = 0; x < map.length; x++) {
				if (seen[x][y])
					result.append(map[x][y]);
				else
					result.append(" ");
			}
		
		return result.toString();
	}
	
	private boolean insideSeenArray(Position pos) {
		return pos.x < seen.length && pos.x >= 0 && pos.y < seen[0].length && pos.y >= 0;
	}
	
}
