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

package com.github.nzyuzin.candiderl.game.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.WeakHashMap;

public final class Position {

    private final int x;
    private final int y;

    // TODO: find library that handles caching properly
    private static final Map<Integer, Map<Integer, Position>> CACHE = new WeakHashMap<>();

    private static final Log log = LogFactory.getLog(Position.class);
    private static int cacheSize = 0;

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

    public static Position getPosition(int x, int y) throws IllegalArgumentException {
        return getPosition(x, y, true);
    }

    public static Position getPosition(int x, int y, boolean fromCache) throws IllegalArgumentException {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(String.format("Position can't be negative! x = %d y = %d", x, y));
        }

        if (!fromCache) {
            return new Position(x, y);
        }

        if (CACHE.containsKey(x)) {
            Map<Integer, Position> y2Position= CACHE.get(x);
            if (y2Position.containsKey(y)) {
                return y2Position.get(y);
            } else {
                Position pos = new Position(x, y);
                y2Position.put(y, pos);
                if (log.isTraceEnabled()) {
                    cacheSize++;
                    log.trace(String.format("created new Position: %s%nTotal size of CACHE is %d", pos, cacheSize));
                }
                return pos;
            }
        } else {
            Position pos = new Position(x, y);
            Map<Integer, Position> y2Position = new WeakHashMap<>();
            y2Position.put(y,pos);
            CACHE.put(x, y2Position);
            if (log.isTraceEnabled()) {
                cacheSize++;
                log.trace(String.format("created new Position: %s%nTotal size of CACHE is %d", pos, cacheSize));
            }
            return pos;
        }
    }

    public double distanceTo(int x, int y) {
        return Math.sqrt((double) ((x - this.x) * (x - this.x))
                + ((y - this.y) * (y - this.y)));
    }

    public double distanceTo(Position that) {
        return distanceTo(that.x, that.y);
    }

    public Position chooseClosest(Position first, Position second) {
        return this.distanceTo(first) < this.distanceTo(second) ? first
                : second;
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