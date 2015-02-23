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

package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.utility.*;

public final class HitGameAction extends AbstractGameAction {

    private GameCharacter target;
    private Map map;

    public HitGameAction(GameCharacter performer, Position pos) {
        super(performer);
        map = performer.getPositionOnMap().getMap();
        target = map.getGameCharacter(pos);
    }

    public boolean canBeExecuted() {
        return target != null && !target.isDead() && !getPerformer().isDead()
                && target.getPosition().distanceTo(getPerformer().getPosition()) < 2;
    }

    public void execute() {
        int damage = getPerformer().roleDamageDice();
        target.takeDamage(damage);
        if (target.isDead()) {
            map.putItem(target.getCorpse(), target.getPosition());
            map.removeGameCharacter(target);
        }
    }
}
