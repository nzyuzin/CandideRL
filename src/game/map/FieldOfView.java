package game.map;

import game.characters.GameCharacter;
import game.utility.*;

import java.util.ArrayDeque;
import java.util.Queue;

public final class FieldOfView {
	private final GameCharacter watcher;
	private boolean[][] seen;
	private int viewDistance;
	
	private void updateFOV() {
		
		Queue<Position> positionsQueue = new ArrayDeque<Position>();
		
		Position pos = null;
		seen = new boolean[Map.getWidthOnScreen()][Map.getHeightOnScreen()];
		boolean[][] transparent = Map.getTransparentCells(watcher.getPosition());
		boolean[][] marked = new boolean[transparent.length][transparent[0].length];
		Direction[] directions = Direction.values();
		Position watcherPos = new Position(seen.length / 2, seen[0].length / 2);
		
		seen[watcherPos.x][watcherPos.y] = true;
		
		for ( int i = 0; i < directions.length; i++ ) {
			positionsQueue.add(DirectionProcessor.applyDirectionToPosition(watcherPos, directions[i]));
			
			while ( !positionsQueue.isEmpty() ) {
				pos = positionsQueue.poll();
				
				if ( !insideSeenArray(pos) || watcherPos.distanceTo(pos) > viewDistance )
					continue;
				
				if ( marked[pos.x][pos.y] )
					continue;
	
				marked[pos.x][pos.y] = true;
				seen[pos.x][pos.y] = true;
				
				if ( transparent[pos.x][pos.y] ) {
					positionsQueue.add(DirectionProcessor
							.applyDirectionToPosition(pos, directions[(i - 1 + directions.length)  % directions.length]));
					positionsQueue.add(DirectionProcessor
							.applyDirectionToPosition(pos, directions[i]));
					positionsQueue.add(DirectionProcessor
							.applyDirectionToPosition(pos, directions[(i + 1) % directions.length]));
				}
			}
		}
	}
	
	private boolean insideSeenArray(Position pos) {
		return pos.x < seen.length && pos.x >= 0 && pos.y < seen[0].length && pos.y >= 0;
	}
	
	public FieldOfView(GameCharacter c, int viewDistance) {
		this.viewDistance = viewDistance;
		watcher = c;
	}
	
	public boolean isSeen(Position mapPos) {
		return true;
	}
	
	public String toString() {
		updateFOV();
		
		char[][] map = Map.getVisibleChars(watcher.getPosition());

		StringBuffer buffer = new StringBuffer();

		for (int y = map[0].length - 1; y >= 0; y--)
			for (int x = 0; x < map.length; x++) {
				if (seen[x][y])
					buffer.append(map[x][y]);
				else
					buffer.append(' ');
			}

		return buffer.toString();
	}
	
	public char[][] toCharArray() {
		updateFOV();
		
		char[][] map = Map.getVisibleChars(watcher.getPosition());

		for (int x = 0; x < map.length; x++)
			for (int y = 0; y < map[0].length; y++) {
				if (!seen[x][y])
					map[x][y] = ' ';
			}

		return map;
	}
	
}
