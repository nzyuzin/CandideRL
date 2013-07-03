/*
 *  This file is part of CandideRL.
 *
 *  CandideRL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CandideRL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.map;

import game.characters.GameCharacter;
import game.ui.GameUI;
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
		boolean[][] transparent = Map.getTransparentCells(watcher.getPosition());
		seen = new boolean[transparent.length][transparent[0].length];
		boolean[][] marked = new boolean[transparent.length][transparent[0].length];
		Direction[] directions = Direction.values();
		Position watcherPos = new Position(seen.length / 2, seen[0].length / 2 - 1);
		
		GameUI.showMessage("HeroPos: " + watcherPos);
		GameUI.showMessage("Real pos: " + Map.getHeroPos(watcher.getPosition()));
		
		seen[watcherPos.x][watcherPos.y] = true;
		
		for (int i = 0; i < directions.length; i++) {
			positionsQueue.add(Direction.applyDirection(watcherPos, directions[i]));
			
			while (!positionsQueue.isEmpty()) {
				pos = positionsQueue.poll();
				
				if (!isInsideSeenArray(pos) || watcherPos.distanceTo(pos) > viewDistance)
					continue;
				
				if (marked[pos.x][pos.y])
					continue;
	
				marked[pos.x][pos.y] = true;
				seen[pos.x][pos.y] = true;
				
				if (transparent[pos.x][pos.y]) {
					positionsQueue.add(Direction
							.applyDirection(pos, directions[(i - 1 + directions.length)  % directions.length]));
					positionsQueue.add(Direction
							.applyDirection(pos, directions[i]));
					positionsQueue.add(Direction
							.applyDirection(pos, directions[(i + 1) % directions.length]));
				}
				else {
					pos = Direction.applyDirection(pos, directions[i]);
					while (isInsideSeenArray(pos)) {
						seen[pos.x][pos.y] = false;
						marked[pos.x][pos.y] = true;
						pos = Direction.applyDirection(pos, directions[i]);
					}
				}
			}
		}
	}
	
	private boolean isInsideSeenArray(Position pos) {
		return pos.x < seen.length && pos.x >= 0 && pos.y < seen[0].length && pos.y >= 0;
	}
	
	public FieldOfView(GameCharacter c, int viewDistance) {
		this.viewDistance = viewDistance;
		watcher = c;
	}
	
	public boolean isSeen(Position mapPos) {
		int x = seen.length + mapPos.x - watcher.getPosition().x;
		int y = seen[0].length + mapPos.y - watcher.getPosition().y;
		
		if (!isInsideSeenArray(new Position(x, y)))
			return false;
		
		return seen[x][y];
	}
	
	public ColoredChar[][] toColoredCharArray() {
		updateFOV();
		
		ColoredChar[][] map = Map.getVisibleChars(watcher.getPosition());

		for (int x = 0; x < map.length; x++)
			for (int y = 0; y < map[0].length; y++) {
				if (!seen[x][y])
					map[x][y] = ColoredChar.NIHIL;
			}

		return map;
	}
	
}
