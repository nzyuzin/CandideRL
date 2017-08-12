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
package com.github.nzyuzin.candiderl.game.utility

object Position {

  private val cacheRange = Range(0, 100)

  private val cache = Array.ofDim[Position](cacheRange.end, cacheRange.end)

  private def init() =
    for (i <- cache.indices)
      for (j <- cache(i).indices)
        cache(i)(j) = new Position(i, j)

  init()

  def getInstance(x: Int, y: Int) = if (cacheRange.contains(x) && cacheRange.contains(y)) cache(x)(y)
                                    else new Position(x, y)
}

class Position private(val xCoordinate: Int, val yCoordinate: Int) {

  def x: Int = xCoordinate
  def y: Int = yCoordinate

  def distanceTo(x: Int, y: Int): Double =
    Math.sqrt(((x - this.x) * (x - this.x)).toDouble + (y - this.y) * (y - this.y))

  def distanceTo(that: Position): Double = distanceTo(that.x, that.y)

  def isAdjacentTo(that: Position): Boolean = this.distanceTo(that) < 2

  /**
    * Returns a position that is closest to this position from given two {@link Position}s
    *
    * @param first  first position
    * @param second second position
    * @return position closestBetweenTwo to this position, either { @param first} or { @param second}
    */
  def closestBetweenTwo(first: Position, second: Position): Position =
    if (this.distanceTo(first) < this.distanceTo(second)) first
    else second

  /**
    * Returns the position that follows this position on the line between this and target positions
    *
    * @param that target position
    * @return position that is on the line between this and target positions
    */
  def nextInLine(that: Position): Position = apply(directionTo(that))

  def apply(direction: Direction): Position = {
    var x = this.x
    var y = this.y
    direction match {
      case Direction.NORTH => y += 1
      case Direction.SOUTH => y -= 1
      case Direction.WEST => x -= 1
      case Direction.EAST => x += 1
      case Direction.NORTHEAST =>
        x += 1
        y += 1
      case Direction.SOUTHEAST =>
        x += 1
        y -= 1
      case Direction.SOUTHWEST =>
        x -= 1
        y -= 1
      case Direction.NORTHWEST =>
        x -= 1
        y += 1
    }
    Position.getInstance(x, y)
  }

  def directionTo(that: Position): Direction = {
    if (this.x == that.x && this.y > that.y) return Direction.SOUTH
    if (this.x == that.x && this.y < that.y) return Direction.NORTH
    if (this.x > that.x && this.y == that.y) return Direction.WEST
    if (this.x < that.x && this.y == that.y) return Direction.EAST
    if (this.x > that.x && this.y < that.y) return Direction.NORTHWEST
    if (this.x < that.x && this.y < that.y) return Direction.NORTHEAST
    if (this.x > that.x && this.y > that.y) return Direction.SOUTHWEST
    if (this.x < that.x && this.y > that.y) return Direction.SOUTHEAST
    null
  }

  override def toString: String = "(" + x + ", " + y + ")"


  override def equals(other: Any): Boolean = other match {
    case that: Position =>
      x == that.x &&
        y == that.y
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(x, y)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}