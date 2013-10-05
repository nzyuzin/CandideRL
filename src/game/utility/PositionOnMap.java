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

package game.utility;

import game.map.Map;

public class PositionOnMap {
    private Position position;
    private Map map;

    private Position lastPos;
    private Map lastMap;

    public PositionOnMap(Position position, Map map) {
        this.position = position;
        this.map = map;
    }

    public Position getPosition() {
        return position;
    }

    public Map getMap() {
        return map;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setPosition(Position position) {
        if (position == null)
            throw new AssertionError();
        this.lastPos = this.position;
        this.position = position;
    }

    public void setMap(Map map) {
        if (map == null)
            throw new AssertionError();
        this.lastMap = this.map;
        this.map = map;
    }

    public Position getLastPos() {
        return lastPos;
    }

    public Map getLastMap() {
        return lastMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionOnMap that = (PositionOnMap) o;

        if (!map.equals(that.map)) return false;
        if (!position.equals(that.position)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (map != null ? map.hashCode() : 0);
        result = 31 * result + (lastPos != null ? lastPos.hashCode() : 0);
        result = 31 * result + (lastMap != null ? lastMap.hashCode() : 0);
        return result;
    }
}
