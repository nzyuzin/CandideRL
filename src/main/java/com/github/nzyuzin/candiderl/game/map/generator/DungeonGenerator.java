/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game.map.generator;

import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.Floor;
import com.github.nzyuzin.candiderl.game.map.cells.Wall;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class DungeonGenerator implements MapGenerator {

    private final int averageRoomWidth;
    private final int averageRoomHeight;
    private final Random random;

    private final EmptyMapGenerator emptyMapGenerator;

    private Map map;

    public DungeonGenerator(final int averageRoomWidth, final int averageRoomHeight) {
        this.averageRoomWidth = averageRoomWidth;
        this.averageRoomHeight = averageRoomHeight;
        this.random = new Random();
        this.emptyMapGenerator = new EmptyMapGenerator(Wall::getWall, Wall::getWall);
    }

    @Override
    public Map generate(final int width, final int height) {
        final Map map = emptyMapGenerator.generate(width, height);
        if (averageRoomWidth > width && averageRoomHeight > height) {
            return map;
        }
        this.map = map;
        final int desiredRoomAmount = (width * height) / (averageRoomWidth * averageRoomHeight) / 4;
        List<Position> generatedRooms = Lists.newArrayList();
        while (generatedRooms.size() < desiredRoomAmount) {
            final int actualRoomWidth = averageRoomWidth + randomDifference(averageRoomWidth);
            final int actualRoomHeight = averageRoomHeight + randomDifference(averageRoomHeight);

            // +1 with -1 inside randoms to avoid walls on map edges and halves of the dimensions
            // to avoid picking a position where a room will collide with a wall
            final int mapColumn = actualRoomWidth / 2 + 1 + random.nextInt(width - actualRoomWidth - 1);
            final int mapRow = actualRoomHeight / 2 + 1 + random.nextInt(height - actualRoomHeight - 1);

            if (isPlacingRoomPossible(mapColumn, mapRow, actualRoomWidth, actualRoomHeight)) {
                placeRoom(mapColumn, mapRow, actualRoomWidth, actualRoomHeight);
                generatedRooms.add(Position.getInstance(mapColumn, mapRow));
            }
        }
        connectRooms(map, generatedRooms);
        return map;
    }

    private boolean isPlacingRoomPossible(int column, int row, int width, int height) {
        for (int i = row / 2; i < row / 2 + height; i++) {
            for (int j = column / 2; j < column / 2 + width; j++) {
                if (map.getCell(i, j) instanceof Floor) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeRoom(int column, int row, int width, int height) {
        for (int i = row / 2; i < row / 2 + height; i++) {
            for (int j = column / 2; j < column / 2 + width; j++) {
                map.setCell(i, j, Floor.getFloor());
            }
        }
    }

    private void connectRooms(final Map map, final List<Position> rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i + 1; j < rooms.size(); j++) {
                drawPassage(map, rooms.get(i), rooms.get(j));
            }
        }
    }

    private void drawPassage(final Map map, final Position pos1, final Position pos2) {
        Position closest = pos1.nextInLine(pos2);
        while (!closest.equals(pos2)) {
            map.setCell(closest, Floor.getFloor());
            closest = closest.nextInLine(pos2);
        }
    }

    private int randomDifference(final int bounds) {
        return bounds / 4 - random.nextInt(bounds / 2 + 1);
    }
}
