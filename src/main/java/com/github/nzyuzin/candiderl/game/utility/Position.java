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

//public class Position {
//
//  private val cacheRange = Range(0, 100)
//
//  private val cache = Array.ofDim[Position](cacheRange.end, cacheRange.end)
//
//  private def init() =
//    for (i <- cache.indices)
//      for (j <- cache(i).indices)
//        cache(i)(j) = new Position(i, j)
//
//  init()
//
//  def getInstance(x: Int, y: Int) = if (cacheRange.contains(x) && cacheRange.contains(y)) cache(x)(y)
//                                    else new Position(x, y)
//}

import java.util.Objects;

public class Position {
  private int x;
  private int y;

  private Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static Position getInstance(int x, int y) {
    return new Position(x, y);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public double distanceTo(int x, int y) {
      return Math.sqrt(((x - this.x) * (x - this.x)) + (y - this.y) * (y - this.y));
  }

  public double distanceTo(Position that) {
    return distanceTo(that.x, that.y);
  }

  public boolean isAdjacentTo(Position that) {
    return this.distanceTo(that) < 2;
  }

  /**
    * Returns a position that is closest to this position from given two {@link Position}s
    *
    * @param first  first position
    * @param second second position
    * @return position closestBetweenTwo to this position, either { @param first} or { @param second}
    */
  public Position closestBetweenTwo(Position first, Position second) {
    if (this.distanceTo(first) < this.distanceTo(second)) return first;
    else return second;
  }

  /**
    * Returns the position that follows this position on the line between this and target positions
    *
    * @param that target position
    * @return position that is on the line between this and target positions
    */
  public Position nextInLine(Position that) {
    return apply(directionTo(that));
  }

  public Position apply(Direction direction) {
    int x = this.x;
    int y = this.y;
    switch (direction) {
      case NORTH: y += 1; break;
      case SOUTH: y -= 1; break;
      case WEST: x -= 1; break;
      case EAST: x += 1; break;
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
    return new Position(x, y);
  }

  public Direction directionTo(Position that) {
    if (this.x == that.x && this.y > that.y) return Direction.SOUTH;
    if (this.x == that.x && this.y < that.y) return Direction.NORTH;
    if (this.x > that.x && this.y == that.y) return Direction.WEST;
    if (this.x < that.x && this.y == that.y) return Direction.EAST;
    if (this.x > that.x && this.y < that.y) return Direction.NORTHWEST;
    if (this.x < that.x && this.y < that.y) return Direction.NORTHEAST;
    if (this.x > that.x && this.y > that.y) return Direction.SOUTHWEST;
    if (this.x < that.x && this.y > that.y) return Direction.SOUTHEAST;
    throw new RuntimeException("Reached return null [this = " + this + "] and [that = " + that + "]");
  }

  @Override
  public String toString() {
    return  "(" + x + ", " + y + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Position) {
      Position that = (Position) other;
      return x == that.x && y == that.y;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}