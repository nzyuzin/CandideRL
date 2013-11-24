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

package game.fov;

import game.fov.strategy.FOVStrategy;
import game.fov.strategy.ShadowCastingStrategy;

import game.characters.GameCharacter;

public class FOVFactory {

    public enum Strategies {
        SHADOW_CASTING(new ShadowCastingStrategy());

        private FOVStrategy implementation;

        private Strategies(FOVStrategy impl) {
            this.implementation = impl;
        }

        FOVStrategy getImplementation() {
            return implementation;
        }
    }

    public static FOVFactory getInstance() {
        return new FOVFactory();
    }

    public FieldOfVision getFOV(GameCharacter watcher, int viewDistance) {
        return new FieldOfVisionImpl(watcher, viewDistance, Strategies.SHADOW_CASTING.getImplementation());
    }

}
