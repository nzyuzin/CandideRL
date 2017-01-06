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

package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.events.CharacterEventContext;
import com.github.nzyuzin.candiderl.game.events.Event;
import com.github.nzyuzin.candiderl.game.events.TakeDamageEvent;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

public class HitInMeleeAction extends AbstractGameAction {

    private GameCharacter target;
    private Map map;

    public HitInMeleeAction(GameCharacter performer, GameCharacter target) {
        super(performer);
        Preconditions.checkNotNull(performer);
        Preconditions.checkNotNull(target);
        this.target = target;
        this.map = performer.getMap();
    }

    public boolean canBeExecuted() {
        return target != null && map.equals(target.getMap()) && !target.isDead() && !getPerformer().isDead()
                && target.getPosition().distanceTo(getPerformer().getPosition()) < 2;
    }

    protected List<Event> doExecute() {
        int damage = getPerformer().rollDamageDice();
        return Lists.newArrayList(new TakeDamageEvent(new CharacterEventContext(target), damage));
    }
}
