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

package com.github.nzyuzin.candiderl.game.utility;

public final class Position {

    private final int x;
    private final int y;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position getInstance(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(String.format("Position can't be negative x = %d y = %d", x, y));
        }
        return new Position(x, y);
    }

    public double distanceTo(int x, int y) {
        return Math.sqrt((double) ((x - this.x) * (x - this.x))
                + (y - this.y) * (y - this.y));
    }

    public double distanceTo(Position that) {
        return distanceTo(that.x, that.y);
    }

    public boolean isAdjacentTo(Position that) {
        return this.distanceTo(that) < 2;
    }

    /**
     * Returns closest to this position from given two {@link Position}s
     *
     * @param first  first position
     * @param second second position
     * @return position closest to this position, either {@param first} or {@param second}
     */
    public Position closest(Position first, Position second) {
        return this.distanceTo(first) < this.distanceTo(second) ? first
                : second;
    }

    public Position apply(Direction direction) {
        int x = getX();
        int y = getY();
        switch (direction) {
            case NORTH:
                y += 1;
                break;
            case SOUTH:
                y -= 1;
                break;
            case WEST:
                x -= 1;
                break;
            case EAST:
                x += 1;
                break;
            case NORTHEAST:
                x += 1;
                y += 1;
                break;
            case SOUTHEAST:
                x += 1;
                y -= 1;
                break;
            case SOUTHWEST:
                x -= 1;
                y -= 1;
                break;
            case NORTHWEST:
                x -= 1;
                y += 1;
                break;
            default:
                break;
        }
        if (x >= 0 && y >= 0) {
            return Position.getInstance(x, y);
        } else {
            return null;
        }
    }

    public Direction directionTo(Position that) {
        if (this.getX() == that.getX() && this.getY() > that.getY())
            return Direction.SOUTH;
        if (this.getX() == that.getX() && this.getY() < that.getY())
            return Direction.NORTH;
        if (this.getX() > that.getX() && this.getY() == that.getY())
            return Direction.WEST;
        if (this.getX() < that.getX() && this.getY() == that.getY())
            return Direction.EAST;
        if (this.getX() > that.getX() && this.getY() < that.getY())
            return Direction.NORTHWEST;
        if (this.getX() < that.getX() && this.getY() < that.getY())
            return Direction.NORTHEAST;
        if (this.getX() > that.getX() && this.getY() > that.getY())
            return Direction.SOUTHWEST;
        if (this.getX() < that.getX() && this.getY() > that.getY())
            return Direction.SOUTHEAST;
        return null;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Position))
            return false;
        return this.x == ((Position) object).getX()
                && this.y == ((Position) object).getY();
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
