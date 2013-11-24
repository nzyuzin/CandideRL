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

package game.characters;

import game.fov.FOVFactory;
import game.fov.FieldOfVision;
import game.utility.ColoredChar;
import game.GameConfig;

import java.util.Map;
import java.util.HashMap;

public final class Player extends GameCharacter {

    private FieldOfVision fov = null;
    private final static Player PLAYER = new Player();

    private final static int PLAYER_INITIAL_MAX_HP = 100;

    private Player() {
        super("Player", "It's you.", PLAYER_INITIAL_MAX_HP);
        this.charOnMap = ColoredChar
                .getColoredChar(game.utility.VisibleCharacters.PLAYER.getVisibleChar(), ColoredChar.WHITE);
        fov = FOVFactory.getInstance().getFOV(this, GameConfig.VIEW_DISTANCE_LIMIT);
    }

    public Map<String, String> getStats() {
        HashMap<String, String> stats = new HashMap<>();
        stats.put("HP", String.valueOf(currentHP));
        stats.put("maxHP", String.valueOf(maxHP));
        stats.put("str", String.valueOf(attributes.strength));
        return stats;
    }

    public ColoredChar[][] getVisibleMap() {
        return fov.getVisibleCells();
    }

    public static Player getInstance() {
        return PLAYER;
    }

}
