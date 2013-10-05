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
import game.utility.*;

import java.util.ArrayDeque;
import java.util.Queue;

public final class FieldOfView {
	private final GameCharacter watcher;
	private boolean[][] seen;
	private int viewDistance;
    private Map map;

    public FieldOfView(GameCharacter c, int viewDistance) {
        this.viewDistance = viewDistance;
        this.watcher = c;
    }

    public boolean isSeen(Position mapPos) {
        int x = seen.length + mapPos.getX() - watcher.getPosition().getX();
        int y = seen[0].length + mapPos.getY() - watcher.getPosition().getY();

        return isInsideSeenArray(Position.getPosition(x, y)) && seen[x][y];
    }

    public ColoredChar[][] toColoredCharArray() {
        updateFOV();

        ColoredChar[][] resultMap = map.getVisibleChars(watcher.getPosition());

        for (int x = 0; x < resultMap.length; x++)
            for (int y = 0; y < resultMap[0].length; y++) {
                if (!seen[x][y])
                    resultMap[x][y] = ColoredChar.NIHIL;
            }

        return resultMap;
    }

	private void updateFOV() {

        map = watcher.getPositionOnMap().getMap();

		Queue<Position> positionsQueue = new ArrayDeque<Position>();
		
		Position pos;
		boolean[][] transparent = map.getTransparentCells(watcher.getPosition());
		seen = new boolean[transparent.length][transparent[0].length];
		boolean[][] marked = new boolean[transparent.length][transparent[0].length];
		Direction[] directions = Direction.values();
		Position watcherPos = Position.getPosition(seen.length / 2, seen[0].length / 2 - 1);
		
		seen[watcherPos.getX()][watcherPos.getY()] = true;
		
		for (int i = 0; i < directions.length; i++) {
			positionsQueue.add(Direction.applyDirection(watcherPos, directions[i]));
			
			while (!positionsQueue.isEmpty()) {
				pos = positionsQueue.poll();
				
				if (!isInsideSeenArray(pos) || watcherPos.distanceTo(pos) > viewDistance)
					continue;
				
				if (marked[pos.getX()][pos.getY()])
					continue;
	
				marked[pos.getX()][pos.getY()] = true;
				seen[pos.getX()][pos.getY()] = true;
				
				if (transparent[pos.getX()][pos.getY()]) {
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
						seen[pos.getX()][pos.getY()] = false;
						marked[pos.getX()][pos.getY()] = true;
						pos = Direction.applyDirection(pos, directions[i]);
					}
				}
			}
		}
	}
	
	private boolean isInsideSeenArray(Position pos) {
		return pos.getX() < seen.length && pos.getX() >= 0 && pos.getY() < seen[0].length && pos.getY() >= 0;
	}
}
