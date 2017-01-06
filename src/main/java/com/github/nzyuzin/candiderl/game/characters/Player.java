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

package com.github.nzyuzin.candiderl.game.characters;

import com.github.nzyuzin.candiderl.game.GameConfig;
import com.github.nzyuzin.candiderl.game.fov.FieldOfVision;
import com.github.nzyuzin.candiderl.game.fov.FovFactory;
import com.github.nzyuzin.candiderl.game.utility.ColoredChar;
import com.github.nzyuzin.candiderl.game.utility.VisibleCharacters;

import java.util.HashMap;
import java.util.Map;

public final class Player extends AbstractGameCharacter {

    private FieldOfVision fov = null;
    private final static Player PLAYER = new Player();

    private Player() {
        super("Player", "Yet another wanderer in forgotten land", DEFAULT_HP);
        this.charOnMap = ColoredChar .getColoredChar(VisibleCharacters.PLAYER.getVisibleChar(), ColoredChar.WHITE);
        fov = FovFactory.getInstance().getFOV(this, GameConfig.VIEW_DISTANCE_LIMIT);
    }

    public Map<String, String> getStats() {
        HashMap<String, String> stats = new HashMap<>();
        stats.put("HP", String.valueOf(currentHP));
        stats.put("maxHP", String.valueOf(maxHP));
        stats.put("str", String.valueOf(attributes.strength));
        return stats;
    }

    public ColoredChar[][] getVisibleMap(int width, int height) {
        return fov.getVisibleCells(width, height);
    }

    public static Player getInstance() {
        return PLAYER;
    }

}
